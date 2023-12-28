package com.example.jeonsilog.view.otheruser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jeonsilog.databinding.ViewCalendarDialogBinding
import com.example.jeonsilog.viewmodel.OtherUserCalendarViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class OtherUserCalendarBottomSheetDialog(private val viewModel: OtherUserCalendarViewModel) : BottomSheetDialogFragment() {
    private var _binding: ViewCalendarDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var date: LocalDate

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ViewCalendarDialogBinding.inflate(inflater, container, false)

        date = LocalDate.of(viewModel.selectedDate.value!!.slice(0..3).toInt(), viewModel.selectedDate.value!!.slice(6..7).toInt(), 1)

        setDateDialog(date)
        setDate()

        binding.tvMove.setOnClickListener {
            viewModel.setSelectedDate(date)
            dismiss()
        }

        binding.npYear.setOnValueChangedListener { _, _, newVal ->
            date = LocalDate.of(newVal, date.monthValue, 1)
            setDateDialog(date)
        }

        binding.npMonth.setOnValueChangedListener { _, _, newVal ->
            date = LocalDate.of(date.year, newVal, 1)
            setDateDialog(date)
        }
        return binding.root
    }
    private fun setDateDialog(date: LocalDate){
        binding.tvDateDialog.text = date.format(DateTimeFormatter.ofPattern("yyyy년 MM월"))
    }
    private fun setDate(){
        val dateYear = date.year
        val dateMonth = date.monthValue

        binding.npMonth.minValue=1
        binding.npMonth.maxValue=12
        binding.npYear.minValue=2020
        binding.npYear.maxValue=2050

        binding.npYear.value=dateYear
        binding.npMonth.value=dateMonth
    }
}