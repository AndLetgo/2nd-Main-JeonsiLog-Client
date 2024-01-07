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

    private var _reviewList = MutableLiveData<MutableList<GetReviewsExhibitionInformationEntity>>()
    val reviewList: LiveData<MutableList<GetReviewsExhibitionInformationEntity>>
        get() = _reviewList
    fun deleteReviewListItem(position:Int){
        _reviewList.value?.removeAt(position)
    }
    fun setReviewList(reviewList:MutableList<GetReviewsExhibitionInformationEntity>){
        _reviewList.value = reviewList
    }
}