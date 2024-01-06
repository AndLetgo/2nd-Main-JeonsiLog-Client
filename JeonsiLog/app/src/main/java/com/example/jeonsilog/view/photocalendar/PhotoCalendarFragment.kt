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
import com.example.jeonsilog.databinding.FragmentPhotoCalendarBinding
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.viewmodel.PhotoCalendarViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class PhotoCalendarFragment() : BaseFragment<FragmentPhotoCalendarBinding>(
    R.layout.fragment_photo_calendar) {
    lateinit var selectedDateCurrent: LocalDate
    lateinit var selectedDate: LocalDate
    override fun init() {

        val viewModel = ViewModelProvider(requireActivity()).get(PhotoCalendarViewModel::class.java)
        binding.lifecycleOwner=requireActivity()
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
        checkPathCalendarOpen()
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
        binding.tbPublicPrivate.setOnClickListener {
            checkPathCalendarOpen()
        }
        binding.tvYearMonth.setOnClickListener {
            val bottomSheetDialogFragment = CalendarBottomDialog(selectedDate,viewModel)
            //bottomSheetDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
            bottomSheetDialogFragment.show(childFragmentManager, bottomSheetDialogFragment.tag)

        }
    }
    private fun checkPathCalendarOpen(){
        runBlocking(Dispatchers.IO) {
            val response = UserRepositoryImpl().patchCalendarOpen(encryptedPrefs.getAT())
            if(response.isSuccessful && response.body()!!.check){
                CoroutineScope(Dispatchers.Main).launch{
                    binding.tbPublicPrivate.isChecked=response.body()!!.information.open
                    if (binding.tbPublicPrivate.isChecked){
                        binding.tbPublicPrivate.setTextColor(resources.getColor(R.color.basic_point))
                    }else{
                        binding.tbPublicPrivate.setTextColor(resources.getColor(R.color.gray_medium))
                    }
                }
            }
        }
    }
    private fun monthYearFromDate(date: LocalDate): String{

        var formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")

        // 받아온 날짜를 해당 포맷으로 변경
        return date.format(formatter)
    }

}