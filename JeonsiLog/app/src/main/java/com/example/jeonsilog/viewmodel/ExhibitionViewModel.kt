package com.example.jeonsilog.viewmodel

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

    private var _currentExhibitionId = MutableLiveData<Int>()
    val currentExhibitionId: LiveData<Int>
        get() = _currentExhibitionId
    fun setCurrentExhibitionId(exhibitionId:Int){
        _currentExhibitionId.value = exhibitionId
    }
}