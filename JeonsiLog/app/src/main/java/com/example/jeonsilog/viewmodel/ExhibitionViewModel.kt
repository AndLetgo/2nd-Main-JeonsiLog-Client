package com.example.jeonsilog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jeonsilog.data.remote.dto.review.GetReviewsExhibitionInformationEntity

class ExhibitionViewModel:ViewModel() {
    private var _exhibitionName = MutableLiveData<String>()
    val exhibitionName:LiveData<String>
        get() = _exhibitionName

    private var _placeName = MutableLiveData<String>()
    val placeName:LiveData<String>
        get() = _placeName

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