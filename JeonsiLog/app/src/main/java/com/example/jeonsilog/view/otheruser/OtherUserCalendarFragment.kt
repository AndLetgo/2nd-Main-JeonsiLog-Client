package com.example.jeonsilog.view.otheruser

import android.os.Bundle

import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentOtherUserCalendarBinding
import com.example.jeonsilog.viewmodel.OtherUserCalendarViewModel
import java.time.LocalDate
import java.time.YearMonth

class OtherUserCalendarFragment: BaseFragment<FragmentOtherUserCalendarBinding>(R.layout.fragment_other_user_calendar) {
    private val viewModel: OtherUserCalendarViewModel by viewModels()
    private lateinit var adapter: OtherUserCalendarRvAdapter
//    private lateinit var gestureDetector: GestureDetector

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
//            override fun onFling(
//                e1: MotionEvent?,
//                e2: MotionEvent,
//                distanceX: Float,
//                distanceY: Float
//            ): Boolean {
//                println(e1 ?: "null")
//                if (e1 != null && e2 != null) {
//                    val deltaY = e2.y - e1.y
//                    println(deltaY)
//                    if (deltaY > 0) {
//                        // 위로 스와이프
//                        viewModel.processVerticalSwipeUp()
//                    } else if (deltaY < 0) {
//                        // 아래로 스와이프
//                        viewModel.processVerticalSwipeDown()
//                    }
//                }
//
//                return true
//            }
//        })
//
//        binding.rvOtherUserCalendar.setOnTouchListener { _, event ->
//            gestureDetector.onTouchEvent(event)
//            true
//        }
    }

    override fun init() {
        binding.vm = viewModel
        binding.lifecycleOwner = requireActivity()
        viewModel.setSelectedDate(LocalDate.now())
        setView()

        binding.rvOtherUserCalendar.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val width = binding.rvOtherUserCalendar.height / 7
                adapter.setLength(width)
                adapter.notifyDataSetChanged()
                binding.clOtherUserCalendar.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        viewModel.selectedDate.observe(this){
            viewModel.getImageList()
            daysInMonthArray(LocalDate.of(viewModel.selectedDate.value!!.slice(0..3).toInt(), viewModel.selectedDate.value!!.slice(6..7).toInt(), 1))
        }

        viewModel.imageList.observe(this){
            adapter.notifyDataSetChanged()
        }

        binding.tvOtherUserYearMonth.setOnClickListener {
            val pickYearMonthDialog = OtherUserCalendarBottomSheetDialog(viewModel)
            pickYearMonthDialog.show(parentFragmentManager, "pickYearMonthDialog")
        }

        binding.ibOtherUserNextMonth.setOnClickListener {
            viewModel.processVerticalSwipeUp()
        }
        binding.ibOtherUserPrevMonth.setOnClickListener {
            viewModel.processVerticalSwipeDown()
        }
    }

    private fun setMonthView() {
        daysInMonthArray(LocalDate.of(viewModel.selectedDate.value!!.slice(0..3).toInt(), viewModel.selectedDate.value!!.slice(6..7).toInt(), 1))
        adapter = OtherUserCalendarRvAdapter(viewModel)
        val manager = GridLayoutManager(requireContext(), 7)
        binding.rvOtherUserCalendar.layoutManager = manager
        binding.rvOtherUserCalendar.adapter = adapter
    }

    private fun daysInMonthArray(date: LocalDate) {
        val dayList = ArrayList<LocalDate?>()
        val yearMonth = YearMonth.from(date)
        val lastDay = yearMonth.lengthOfMonth()
        val firstDay = date.withDayOfMonth(1)
        val dayOfWeek = firstDay.dayOfWeek.value

        for (i in 1 until 42) {
            if (i <= dayOfWeek || i > lastDay + dayOfWeek) {
                dayList.add(null)
            } else {
                dayList.add(LocalDate.of(date.year, date.month, i - dayOfWeek))
            }
        }

        viewModel.setDayList(dayList)
    }

    private fun setView(){
        if (!viewModel.isPublic.value!!) {
            binding.ivOtherUserCalendarEmptyImg.visibility = View.VISIBLE
            binding.tvOtherUserCalendarEmptyTitle.visibility = View.VISIBLE
            binding.tbOtherUserCalendar.visibility = View.GONE
            binding.rvOtherUserCalendar.visibility = View.GONE
            binding.llOtherUserCalendar.visibility = View.GONE
        } else {
            binding.ivOtherUserCalendarEmptyImg.visibility = View.GONE
            binding.tvOtherUserCalendarEmptyTitle.visibility = View.GONE
            binding.tbOtherUserCalendar.visibility = View.VISIBLE
            binding.rvOtherUserCalendar.visibility = View.VISIBLE
            binding.llOtherUserCalendar.visibility = View.VISIBLE

            setMonthView()
        }
    }
}