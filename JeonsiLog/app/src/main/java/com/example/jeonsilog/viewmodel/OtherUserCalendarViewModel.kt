package com.example.jeonsilog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jeonsilog.data.remote.dto.calendar.GetPhotoInformation
import com.example.jeonsilog.repository.calendar.CalendarRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OtherUserCalendarViewModel: ViewModel() {
    private var _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String>
        get() = _selectedDate

    fun setSelectedDate(date: LocalDate){
        _selectedDate.value = date.format(DateTimeFormatter.ofPattern("yyyy년 MM월"))
    }

    private var _imageList = MutableLiveData<List<GetPhotoInformation>>()
    val imageList: LiveData<List<GetPhotoInformation>>
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

    fun nextMonth() {
        setSelectedDate(LocalDate.of(selectedDate.value!!.slice(0..3).toInt(), selectedDate.value!!.slice(6..7).toInt(), 1).plusMonths(1))
    }

    fun prevMonth() {
        setSelectedDate(LocalDate.of(selectedDate.value!!.slice(0..3).toInt(), selectedDate.value!!.slice(6..7).toInt(), 1).minusMonths(1))
    }

    fun getImageList(otherUserId: Int){
        val date = LocalDate.of(selectedDate.value!!.slice(0..3).toInt(), selectedDate.value!!.slice(6..7).toInt(), 1).format(DateTimeFormatter.ofPattern("yyyyMM"))

        runBlocking(Dispatchers.IO){
            val response = CalendarRepositoryImpl().getOtherPhotoMonth(GlobalApplication.encryptedPrefs.getAT(), otherUserId, date)
            if(response.isSuccessful && response.body()!!.check){
                val temp = response.body()!!.information.listIterator()
                val list = mutableListOf<GetPhotoInformation>()
                while(temp.hasNext()){
                    list.add(temp.next())
                }

                viewModelScope.launch(Dispatchers.Main) {
                    _imageList.value = list
                }
            }
        }
    }
}