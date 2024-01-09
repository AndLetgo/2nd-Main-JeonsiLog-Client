package com.example.jeonsilog.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jeonsilog.data.remote.dto.review.GetReviewsExhibitionInformationEntity

class ExhibitionViewModel: ViewModel() {
    private var _reviewInfo = MutableLiveData<GetReviewsExhibitionInformationEntity>()
    val reviewInfo : LiveData<GetReviewsExhibitionInformationEntity>
        get() = _reviewInfo

    fun setReviewInfo(data:GetReviewsExhibitionInformationEntity){
        _reviewInfo.value = data
    }

    private var _currentExhibitionIds = MutableLiveData<MutableList<Int>>()
    val currentExhibitionIds: LiveData<MutableList<Int>>
        get() = _currentExhibitionIds
    fun setCurrentExhibitionIds(exhibitionId:Int){
        _currentExhibitionIds.value = mutableListOf(exhibitionId)
    }
    fun addCurrentExhibitionId(exhibitionId:Int){
        _currentExhibitionIds.value?.add(exhibitionId)
        Log.d("TAG", "addCurrentExhibitionId: ${_currentExhibitionIds.value}")
        Log.d("TAG", "addCurrentExhibitionId: size: ${getCurrentExhibitionsSize()}")
    }
    fun getCurrentExhibitionsSize():Int{
        return _currentExhibitionIds.value!!.size
    }
    fun removeLastExhibitionId(){
        if(getCurrentExhibitionsSize()>0){
            _currentExhibitionIds.value?.removeAt(getCurrentExhibitionsSize()-1)
            Log.d("TAG", "removeLastExhibitionId: size: ${getCurrentExhibitionsSize()}")
        }
    }
}