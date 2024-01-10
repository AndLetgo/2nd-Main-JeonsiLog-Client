package com.example.jeonsilog.view.photocalendar

import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.user.PatchCalendarOpenResponse
import com.example.jeonsilog.databinding.FragmentPhotoCalendarBinding
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.viewmodel.PhotoCalendarViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isRefresh
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.log

class PhotoCalendarFragment : BaseFragment<FragmentPhotoCalendarBinding>(
    R.layout.fragment_photo_calendar) {
    lateinit var selectedDateCurrent: LocalDate
    lateinit var selectedDate: LocalDate
    override fun init() {

        val viewModel = ViewModelProvider(requireActivity()).get(PhotoCalendarViewModel::class.java)
        binding.lifecycleOwner=requireActivity()
        isRefresh.observe(this){
            if(it){
                (activity as MainActivity).refreshFragment(PhotoCalendarFragment())
                isRefresh.value = false
            }
        }
        val currentItem = Int.MAX_VALUE / 2
        selectedDateCurrent  = LocalDate.now()
        selectedDate  = LocalDate.now()
        viewModel.setCurrentDate(selectedDateCurrent)
        viewModel.currentDate.observe(viewLifecycleOwner, Observer { newDate ->
            val monthDifference = ChronoUnit.MONTHS.between(selectedDateCurrent, newDate).toInt()
            val tt=currentItem+monthDifference
            binding.vpPhotoCalendar.setCurrentItem(tt, false)

        })
        binding.tvYearMonth.text=monthYearFromDate(selectedDateCurrent)
        val adapter = InfinitePagerAdapter(requireActivity())
        binding.vpPhotoCalendar.adapter = adapter
        binding.vpPhotoCalendar.offscreenPageLimit = 1

        binding.vpPhotoCalendar.setCurrentItem(currentItem, false)
        binding.vpPhotoCalendar.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                //// Handle page selection if needed
                var did=currentItem-position
                if(did>0){
                    selectedDate = selectedDateCurrent.minusMonths(did.toLong())
                }else if(did<0){
                    did=-did
                    selectedDate = selectedDateCurrent.plusMonths(did.toLong())
                }else{
                    selectedDate=selectedDateCurrent
                }
                binding.tvYearMonth.text=monthYearFromDate(selectedDate)
            }
        })
        checkPathCalendarOpen()
        binding.tbPublicPrivate.setOnClickListener {
            clickCalendarOpen()
        }
        binding.tvYearMonth.setOnClickListener {
            val bottomSheetDialogFragment = CalendarBottomDialog(selectedDate,viewModel)
            bottomSheetDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
            bottomSheetDialogFragment.show(childFragmentManager, bottomSheetDialogFragment.tag)

        }

    }
    private fun checkPathCalendarOpen(){
        runBlocking(Dispatchers.IO) {
            val myId=encryptedPrefs.getUI()
            val response = UserRepositoryImpl().getIsOpen(encryptedPrefs.getAT(),myId)
            if(response.isSuccessful && response.body()!!.check){
                if (response.body()!!.information.isOpen){
                    //포토캘린더 공개 설정
                    binding.tbPublicPrivate.isChecked=true
                    binding.tbPublicPrivate.setTextColor(resources.getColor(R.color.basic_point))
                }else{
                    //포토캘린더 비공개 설정
                    binding.tbPublicPrivate.isChecked=false
                    binding.tbPublicPrivate.setTextColor(resources.getColor(R.color.gray_medium))
                }
            }
        }
    }
    private fun clickCalendarOpen(){
        runBlocking(Dispatchers.IO) {
            val response01 = UserRepositoryImpl().patchCalendarOpen(encryptedPrefs.getAT())
        }
        checkPathCalendarOpen()
    }
    private fun monthYearFromDate(date: LocalDate): String{

        var formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")

        // 받아온 날짜를 해당 포맷으로 변경
        return date.format(formatter)
    }

}