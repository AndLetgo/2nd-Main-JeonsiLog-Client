package com.example.jeonsilog.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jeonsilog.data.remote.dto.review.GetReviewsExhibitionInformationEntity

class ExhibitionViewModel: ViewModel() {
    private var _currentExhibitionIds = MutableLiveData<MutableList<Int>>()
    val currentExhibitionIds: LiveData<MutableList<Int>>
        get() = _currentExhibitionIds
    fun setCurrentExhibitionIds(exhibitionId:Int){
        _currentExhibitionIds.value = mutableListOf(exhibitionId)
    }
    fun addCurrentExhibitionId(exhibitionId:Int){
        _currentExhibitionIds.value?.add(exhibitionId)
    }
    fun getCurrentExhibitionsSize():Int{
        return _currentExhibitionIds.value!!.size
    }
    fun removeLastExhibitionId(){
        if(getCurrentExhibitionsSize()>0){
            _currentExhibitionIds.value?.removeAt(getCurrentExhibitionsSize()-1)
        }
    }

    private var _userReview = MutableLiveData("")
    val userReview: LiveData<String>
        get() = _userReview
    fun setUserReview(review: String){
        _userReview.value = review
    }

    private var _replyCount = MutableLiveData(0)
    val replyCount: LiveData<Int>
        get() = _replyCount
    fun setReplyCount(count: Int){
        _replyCount.value = count
    }

    private var _reviewItem = MutableLiveData<UpdateReviewItem>()
    val reviewItem: LiveData<UpdateReviewItem>
        get() = _reviewItem
    fun setReviewItem(review:UpdateReviewItem){
        _reviewItem.value = review
    }
}
data class UpdateReviewItem(
    val item:GetReviewsExhibitionInformationEntity,
    val position: Int
)