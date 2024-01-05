package com.example.jeonsilog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class PhotoCalendarViewModel: ViewModel() {
    private val _currentDate = MutableLiveData<LocalDate>()
    val currentDate: LiveData<LocalDate> get() = _currentDate
    // 값을 업데이트하는 함수
    fun setCurrentDate(newDate: LocalDate) {
        _currentDate.value = newDate
    }


}