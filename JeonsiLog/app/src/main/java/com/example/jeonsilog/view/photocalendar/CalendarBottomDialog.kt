package com.example.jeonsilog.view.photocalendar


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.jeonsilog.databinding.ViewCalendarDialogBinding
import com.example.jeonsilog.viewmodel.PhotoCalendarViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class CalendarBottomDialog(var date: LocalDate,val viewModel: PhotoCalendarViewModel) : BottomSheetDialogFragment() {
    private var _binding: ViewCalendarDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ViewCalendarDialogBinding.inflate(inflater, container, false)
        setDateDialog(date)
        setDate()
        dialog?.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        //이동버튼
        binding.tvMove.setOnClickListener {
            //뷰모델에 선택 날짜 저장
            viewModel.setCurrentDate(date)
            dismiss()
        }
        //연 관련 넘버피커
        binding.npYear.setOnValueChangedListener { picker, oldVal, newVal ->
            //년 설정
            date=LocalDate.of(newVal, date.monthValue, 1)
            // 값이 변경될 때의 동작
            setDateDialog(date)

        }
        //월 관련 넘버피커
        binding.npMonth.setOnValueChangedListener { picker, oldVal, newVal ->
            //월 설정
            date=LocalDate.of(date.year, newVal, 1)
            // 값이 변경될 때의 동작
            setDateDialog(date)
        }
        return binding.root
    }
    fun setDateDialog(Date:LocalDate){
        //받아온 날짜 텍스트 반영
        binding.tvDateDialog.text=monthYearFromDate(Date)
    }
    fun setDate(){
        //받아온 날짜 변수 저장
        var dateYear=date.year
        var dateMonth=date.monthValue
        //넘버피커 최대 최소값 설정
        binding.npMonth.minValue=1
        binding.npMonth.maxValue=12
        binding.npYear.minValue=2000
        binding.npYear.maxValue=2050
        //넘버피커 세팅
        binding.npYear.value=dateYear
        binding.npMonth.value=dateMonth
    }
    private fun monthYearFromDate(date: LocalDate): String{
        //형식반환
        var formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")
        // 받아온 날짜를 해당 포맷으로 변경
        return date.format(formatter)
    }
}