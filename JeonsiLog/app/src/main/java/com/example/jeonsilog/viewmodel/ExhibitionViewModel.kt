package com.example.jeonsilog.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jeonsilog.data.remote.dto.review.CheckReviewEntity
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
    fun setReviewItemNumReply(plus:Boolean){
        val item = reviewItem.value
        if(plus){
            item!!.item.numReply++
        }else{
            item!!.item.numReply--
        }
        _reviewItem.value = item!!
    }

    private var _myReviewItem = MutableLiveData<UpdateReviewItem>()
    val myReviewItem: LiveData<UpdateReviewItem>
        get() = _myReviewItem
    fun setMyReviewItem(review:UpdateReviewItem){
        _myReviewItem.value = review
    }
    fun resetMyReviewItem(){
        _myReviewItem = MutableLiveData<UpdateReviewItem>()
    }

    private var _checkReviewEntity = MutableLiveData<CheckReviewEntity>()
    val checkReviewEntity : LiveData<CheckReviewEntity>
        get() = _checkReviewEntity
    fun setCheckReviewEntity(reviewEntity: CheckReviewEntity){
        _checkReviewEntity.value = reviewEntity
    }
    fun resetCheckReviewEntity(){
        Log.d("TAG", "resetCheckReviewEntity: ")
        _checkReviewEntity = MutableLiveData<CheckReviewEntity>()
    }

    private var _checkReviewDelete = MutableLiveData(false)
    val checkReviewDelete: LiveData<Boolean>
        get() = _checkReviewDelete
    fun setCheckReviewDelte(check:Boolean){
        _checkReviewDelete.value = check
    }

    //viewTreeObserver 대신 사용
    private var _information = MutableLiveData<String>()
    val information: LiveData<String>
        get() = _information
    fun setInformation(info:String){
        _information.value = info
    }

    //댓글
    private var _checkCount = MutableLiveData(false)
    val checkCount: LiveData<Boolean>
        get() = _checkCount
    fun setCheckCount(check:Boolean){
        _checkCount.value = check
    }
}
data class UpdateReviewItem(
    val item:GetReviewsExhibitionInformationEntity,
    val position: Int
)