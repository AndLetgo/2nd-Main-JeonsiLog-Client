package com.example.jeonsilog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jeonsilog.view.otheruser.OtherUserCalendarModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OtherUserCalendarViewModel: ViewModel() {
    private var _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String>
        get() = _selectedDate

    fun setSelectedDate(date: LocalDate){
        _selectedDate.value = date.format(DateTimeFormatter.ofPattern("yyyy년 MM월"))
    }

    private var _imageList = MutableLiveData<List<OtherUserCalendarModel>>()
    val imageList: LiveData<List<OtherUserCalendarModel>>
        get() = _imageList

    private var _dayList = MutableLiveData<ArrayList<LocalDate?>>()
    val dayList: LiveData<ArrayList<LocalDate?>>
        get() = _dayList

    fun setDayList(p: ArrayList<LocalDate?>){
        _dayList.value = p
    }

    private var _isPublic = MutableLiveData<Boolean>(true)
    val isPublic: LiveData<Boolean>
        get() = _isPublic

    fun processVerticalSwipeUp() {
        println("up: ${selectedDate.value}")
        setSelectedDate(LocalDate.of(selectedDate.value!!.slice(0..3).toInt(), selectedDate.value!!.slice(6..7).toInt(), 1).plusMonths(1))
    }

    fun processVerticalSwipeDown() {
        println("down: ${selectedDate.value}")
        setSelectedDate(LocalDate.of(selectedDate.value!!.slice(0..3).toInt(), selectedDate.value!!.slice(6..7).toInt(), 1).minusMonths(1))
    }

    fun getImageList(){
        // 테스트 데이터
        println(selectedDate.value)
        when (selectedDate.value) {
            "2023년 12월" -> {
                _imageList.value = listOf(
                    OtherUserCalendarModel(LocalDate.of(2023, 12, 5), "https://www.sac.or.kr/site/main/file/manage/49253"),
                    OtherUserCalendarModel(LocalDate.of(2023, 12, 23), "https://www.sac.or.kr/site/main/file/manage/60840"),
                    OtherUserCalendarModel(LocalDate.of(2023, 12, 31), "https://www.sac.or.kr/site/main/file/manage/49176")
                )
            }
            "2024년 01월" -> {
                _imageList.value = listOf(
                    OtherUserCalendarModel(LocalDate.of(2024, 1, 2), "https://picsum.photos/id/237/200/300"),
                    OtherUserCalendarModel(LocalDate.of(2024, 1, 14), "https://picsum.photos/id/237/200/300"),
                    OtherUserCalendarModel(LocalDate.of(2024, 1, 24), "https://picsum.photos/id/237/200/300")
                )
            }
            "2023년 11월" -> {
                _imageList.value = listOf(
                    OtherUserCalendarModel(LocalDate.of(2023, 11, 4), "https://picsum.photos/id/237/200/300"),
                    OtherUserCalendarModel(LocalDate.of(2023, 11, 11), "https://picsum.photos/id/237/200/300"),
                    OtherUserCalendarModel(LocalDate.of(2023, 11, 17), "https://picsum.photos/id/237/200/300")
                )
            }
            else -> {
                _imageList.value = listOf()
            }
        }

        if(!imageList.value.isNullOrEmpty()){
            for (i in imageList.value!!){
                println(i)
            }
        }
    }
}