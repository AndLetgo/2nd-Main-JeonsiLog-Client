package com.example.jeonsilog.view.photocalendar

import android.util.Log
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
import com.example.jeonsilog.databinding.FragmentPhotoCalendarBinding
import com.example.jeonsilog.viewmodel.PhotoCalendarViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class PhotoCalendarFragment(val viewModel: PhotoCalendarViewModel) : BaseFragment<FragmentPhotoCalendarBinding>(
    R.layout.fragment_photo_calendar) {
    lateinit var selectedDateCurrent: LocalDate
    lateinit var selectedDate: LocalDate
    override fun init() {
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
        val adapter = InfinitePagerAdapter(requireActivity(),viewModel)
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
        binding.tbPublicPrivate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // ToggleButton이 On인 경우의 동작
                // 예: 텍스트 색상 변경
                binding.tbPublicPrivate.setTextColor(resources.getColor(R.color.basic_point))
            } else {
                // ToggleButton이 Off인 경우의 동작
                // 예: 텍스트 색상을 다시 기본 색상으로 변경
                binding.tbPublicPrivate.setTextColor(resources.getColor(R.color.gray_medium))
            }
        }
        binding.tvYearMonth.setOnClickListener {
            val bottomSheetDialogFragment = CalendarBottomDialog(selectedDate,viewModel)
            bottomSheetDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
            bottomSheetDialogFragment.show(childFragmentManager, bottomSheetDialogFragment.tag)

        }
    }
    private fun monthYearFromDate(date: LocalDate): String{

        var formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")

        // 받아온 날짜를 해당 포맷으로 변경
        return date.format(formatter)
    }

}