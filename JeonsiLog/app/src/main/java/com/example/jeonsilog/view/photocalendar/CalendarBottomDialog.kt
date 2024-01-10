package com.example.jeonsilog.view.photocalendar


import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.jeonsilog.databinding.ViewCalendarDialogBinding
import com.example.jeonsilog.viewmodel.PhotoCalendarViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class CalendarBottomDialog(var date: LocalDate,val viewModel: PhotoCalendarViewModel) : DialogFragment() {
    private var _binding: ViewCalendarDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ViewCalendarDialogBinding.inflate(inflater, container, false)
        setDateDialog(date)
        setDate()
        setDimClick()
        //dialog?.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()

    }
    fun setView(){
        // 다이얼로그의 배경을 투명하게 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 다이얼로그의 외부 터치 이벤트 처리 (다이얼로그가 닫히지 않도록 함)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                // 뒤로가기 버튼이 눌렸을 때 수행할 동작
                dismiss()
                true
            } else {
                false
            }
        }

        // 전체 화면으로 다이얼로그 표시 (다이얼로그 내용물의 크기를 외부까지 확장)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
        // 다이얼로그의 내용물이 차지하는 레이아웃의 크기를 조정
        val params = binding.root.layoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.root.layoutParams = params
    }
    fun setDimClick(){
        binding.ivDimmingZone.setOnClickListener {
            dismiss()
        }
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