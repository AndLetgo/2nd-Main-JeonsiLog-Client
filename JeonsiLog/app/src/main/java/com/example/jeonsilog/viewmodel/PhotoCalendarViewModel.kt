package com.example.jeonsilog.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jeonsilog.data.remote.dto.PhotoCalendarItem
import com.example.jeonsilog.view.photocalendar.Test_Item
import java.time.LocalDate

class PhotoCalendarViewModel: ViewModel() {
    private val _currentDate = MutableLiveData<LocalDate>()
    val currentDate: LiveData<LocalDate> get() = _currentDate

    // 값을 업데이트하는 함수
    fun setCurrentDate(newDate: LocalDate) {
        _currentDate.value = newDate
    }

    // PhotoCalendarItem을 리스트 요소로 가진 라이브데이터
    private val _photoCalendarItemList = MutableLiveData<List<PhotoCalendarItem>>()
    val photoCalendarItemList get() = _photoCalendarItemList
    // 이미지를 PhotoCalendarItemList에 추가하는 함수
    fun addItemToPhotoCalendarList(item: PhotoCalendarItem) {
        // 현재 PhotoCalendarItemList을 가져옴
        val currentList = _photoCalendarItemList.value.orEmpty().toMutableList()

        // 새로운 아이템을 리스트에 추가
        currentList.add(item)

        // 뷰모델을 통해 PhotoCalendarItemList 갱신
        _photoCalendarItemList.value = currentList
    }
    fun removeItemByPosition(cellDate:String) {
        val currentList = _photoCalendarItemList.value.orEmpty().toMutableList()

        // Img_Position과 position이 동일한 요소를 찾아 제거
        currentList.removeAll { it.Img_Position == cellDate }

        // 업데이트된 리스트를 LiveData에 할당
        _photoCalendarItemList.value = currentList
    }

    var exhibitionlist= listOf(
        Test_Item("https://picsum.photos/id/11/200/300","Becoming Winter : [ ]","서울특별시 서초구","크리스앤코 갤러리","전시 중",null),
        Test_Item("https://picsum.photos/id/24/200/300","우린 모두 다른 우주에서","서울특별시 강남구","아르떼케이","시작전",null),
        Test_Item("https://picsum.photos/id/36/200/300","프레리 : Happiness is Everywhere","서울특별시 종로구","나인원갤러리 / 착한갤러리 리세일","시작 전","무료"),
        Test_Item("https://picsum.photos/id/42/200/300","2023 춘천 모두의 미술","강원도 춘천","춘천문화예술회관","전시 중","무료"),
    )
}