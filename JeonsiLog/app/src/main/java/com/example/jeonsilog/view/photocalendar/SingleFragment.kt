package com.example.jeonsilog.view.photocalendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.calendar.GetPhotoInformation
import com.example.jeonsilog.data.remote.dto.calendar.GetPhotoResponse
import com.example.jeonsilog.databinding.FragmentSingleBinding
import com.example.jeonsilog.repository.calendar.CalendarRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class SingleFragment(val Position:Int) : Fragment(),
    OnItemListener,LoadBottomDialog.OnDismissListener,CommunicationListener {
    val currentItem = Int.MAX_VALUE / 2
    private var _binding: FragmentSingleBinding? = null
    private val binding get() = _binding!!

    var adapter: PhotoCalendatAdapter? = null
    var bottomSheetDialogFragment: LoadBottomDialog?=null

    //년월 변수
    lateinit var selectedDate: LocalDate
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSingleBinding.inflate(inflater, container, false)
        //현재 날짜 저장
        selectedDate  = LocalDate.now()
        // PhotoCalendarViewModel의 photoCalendarItemList에 옵저버 등록
        checkPosition()
        setMonthView()



        return binding.root

    }
    fun setMonthView() {

        //어댑터 초기화
        var yearMonth = yearMonthFromDate(selectedDate)
        lateinit var  list: List<GetPhotoInformation>
        val response: Response<GetPhotoResponse>
        runBlocking(Dispatchers.IO) {
            response = CalendarRepositoryImpl().getMyPhotoMonth(encryptedPrefs.getAT(),yearMonth)
        }
        if(response.isSuccessful && response.body()!!.check){
            list= response.body()!!.information

        } else {
            list= listOf<GetPhotoInformation>()

        }
        adapter = context?.let { PhotoCalendatAdapter(dayInMonthArray(selectedDate) ,this, it , list  , selectedDate) }
        //레이아웃 설정(열 7개)
        var manager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 7)
        //레이아웃 적용
        binding.rvPhotoCalendarSingle.layoutManager = manager
        //어댑터 적용
        binding.rvPhotoCalendarSingle.adapter = adapter

    }

    private fun yearMonthFromDate(date: LocalDate): String{
        var formatter = DateTimeFormatter.ofPattern("yyyyMM")
        // 받아온 날짜를 해당 포맷으로 변경
        return date.format(formatter)
    }


    //날짜 생성
    private fun dayInMonthArray(date: LocalDate): ArrayList<String>{
        var dayList = ArrayList<String>()
        var yearMonth = YearMonth.from(date)
        //해당 월 마지막 날짜 가져오기(예: 28, 30, 31)
        var lastDay = yearMonth.lengthOfMonth()
        //해당 월의 첫 번째 날 가져오기(예: 4월 1일)
        var firstDay = selectedDate.withDayOfMonth(1)
        //첫 번째날 요일 가져오기(월:1, 일: 7)
        var dayOfWeek = firstDay.dayOfWeek.value
        for(i in 1..41){
            if(i <= dayOfWeek || i > (lastDay + dayOfWeek)){
                dayList.add("")
            }else{
                dayList.add((i - dayOfWeek).toString())
            }
        }
        return dayList
    }

    //SingleFragment상의 년월 조회
    fun checkPosition(){
        var did=currentItem-Position
        if(did>0){
            selectedDate = selectedDate.minusMonths(did.toLong())
        }else if(did<0){
            did=-did
            selectedDate = selectedDate.plusMonths(did.toLong())
        }
    }
    //아이템 클릭 이벤트
    override fun onItemClick(itemDate: LocalDate) {
        if (itemDate<=LocalDate.now()){
            bottomSheetDialogFragment = LoadBottomDialog(itemDate,this)
            bottomSheetDialogFragment!!.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
            bottomSheetDialogFragment!!.setOnDismissListener(this)
            bottomSheetDialogFragment!!.show(childFragmentManager, bottomSheetDialogFragment!!.tag)
        }
    }
    override fun onDismiss() {
        bottomSheetDialogFragment!!.dismiss()
    }
    override fun onDialogButtonClick(data: String) {
        setMonthView()
    }
    override fun onRecyclerViewItemClick(position: Int) {
        setMonthView()
    }
}