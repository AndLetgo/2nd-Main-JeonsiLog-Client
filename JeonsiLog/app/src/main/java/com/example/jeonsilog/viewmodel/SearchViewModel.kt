package com.example.jeonsilog.viewmodel

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.ExhibitionInfoItem
import com.example.jeonsilog.data.remote.dto.ExhibitionPlaceItem
import com.example.jeonsilog.data.remote.dto.ExhibitionRandom
import com.example.jeonsilog.data.remote.dto.UserSearchItem

class SearchViewModel  : ViewModel() {

    //=======================================================================================//
    // MutableLiveData를 사용하여 변경 가능한 리스트를 LiveData로 감싸기
    private val itemListLiveData = MutableLiveData<List<String>>()
    // ViewModel 밖에서는 LiveData로만 접근 가능하도록 노출
    val itemlist: LiveData<List<String>> get() = itemListLiveData


    // 리스트 업데이트하는 메서드
    fun updateItemList(newItemList: List<String>) {
        itemListLiveData.value = newItemList
    }
    fun removeItemAt(index: Int) {
        val currentList = itemListLiveData.value.orEmpty().toMutableList()

        if (index in 0 until currentList.size) {
            currentList.removeAt(index)

            // LiveData를 업데이트
            itemListLiveData.value = currentList
            Log.d("내부테스트", "동일 검색기록 삭제")
        }
    }
    //=======================================================================================//
    val search_record_title = ObservableField<String>()
    fun updateText(newText: String) {
        search_record_title.set(newText)
    }
    var randomExhibitionList= arrayListOf<ExhibitionRandom>()



}
