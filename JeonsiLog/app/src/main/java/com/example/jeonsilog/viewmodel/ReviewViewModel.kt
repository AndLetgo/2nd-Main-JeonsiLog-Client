package com.example.jeonsilog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jeonsilog.data.remote.dto.review.GetReviewsExhibitionInformationEntity

class ReviewViewModel: ViewModel() {
    private var _reviewInfo = MutableLiveData<GetReviewsExhibitionInformationEntity>()
    val reviewInfo : LiveData<GetReviewsExhibitionInformationEntity>
        get() = _reviewInfo

    fun setReviewInfo(data:GetReviewsExhibitionInformationEntity){
        _reviewInfo.value = data
    }
}