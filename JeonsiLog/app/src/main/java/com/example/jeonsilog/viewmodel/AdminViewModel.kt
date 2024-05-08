package com.example.jeonsilog.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jeonsilog.data.remote.dto.exhibition.ExhibitionInfo
import com.example.jeonsilog.data.remote.dto.exhibition.SearchInformationEntity
import com.example.jeonsilog.data.remote.dto.place.SearchPlacesInformationEntity
import com.example.jeonsilog.data.remote.dto.user.SearchUserInformationEntity
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.repository.place.PlaceRepositoryImpl
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminViewModel:ViewModel() {
    //현재 Admin || User 화면 체크
    private var _isAdminPage = MutableLiveData<Boolean>()
    val isAdminPage : LiveData<Boolean>
        get() = _isAdminPage
    fun setIsAdminPage(check:Boolean){
        _isAdminPage.value = check
    }

    //수정 체크
    private var _isChanged = MutableLiveData(false)
    val isChanged: LiveData<Boolean> get() = _isChanged
    fun setIsChanged(check:Boolean){ _isChanged.value = check
        Log.d("admin", "setIsChanged: ${isChanged.value}")
    }

    //전시회 정보
    private var _exhibitionInfo = MutableLiveData<ExhibitionInfo?>()
    val exhibitionInfo : LiveData<ExhibitionInfo?>
        get() = _exhibitionInfo
    fun setExhibitionInfo(data: ExhibitionInfo){
        _exhibitionInfo.value = data
    }
    //기타 정보
    private var _exhibitionName = MutableLiveData<String>()
    val exhibitionName: LiveData<String> get()= _exhibitionName
    fun setExhibitionName(name:String){ _exhibitionName.value = name }

    private var _placeName = MutableLiveData<String?>()
    val placeName: LiveData<String?> get()= _placeName
    fun setPlaceName(name:String?){ _placeName.value = name }

    private var _placeAddress = MutableLiveData<String?>()
    val placeAddress : LiveData<String?> get() = _placeAddress
    fun setAddress(address: String?){ _placeAddress.value = address }

    private var _placeCall = MutableLiveData<String?>()
    val placeCall : LiveData<String?> get() = _placeCall
    fun setPlaceCall(call:String?){ _placeCall.value = call }

    private var _placeHomepage = MutableLiveData<String?>()
    val placeHomepage : LiveData<String?> get() = _placeHomepage
    fun setPlaceHomepage(homepage:String?){ _placeHomepage.value = homepage }

    private var _exhibitionInformation = MutableLiveData<String?>()
    val exhibitionInformation : LiveData<String?> get() = _exhibitionInformation
    fun setExhibitionInformation(information:String?){ _exhibitionInformation.value = information }

    //감상평 정보
    private var _reviewItem = MutableLiveData<UpdateReviewItem?>()
    val reviewItem: LiveData<UpdateReviewItem?>
        get() = _reviewItem
    fun setReviewItem(review:UpdateReviewItem?){
        _reviewItem.value = review
    }
    private var _deletedReviewPosition = MutableLiveData<Int?>()
    val deletedReviewPosition : LiveData<Int?>
        get() = _deletedReviewPosition
    fun setDeletedReviewPosition(position:Int?){
        _deletedReviewPosition.value = position
    }

    //포스터
    private var _exhibitionPosterImg = MutableLiveData<String?>()
    val exhibitionPosterImg : LiveData<String?>
        get() = _exhibitionPosterImg
    fun setExhibitionPosterImg(posterImg:String?){
        _exhibitionPosterImg.value = posterImg
    }
    //포스터 Uri
    private var _posterUri = MutableLiveData<Uri>()
    val posterUri : LiveData<Uri>
        get() = _posterUri
    fun setPosterUri(uri: Uri){
        _posterUri.value = uri
    }
    fun resetExhibitionInfo(){
        _exhibitionName = MutableLiveData<String>()
        _placeName = MutableLiveData<String?>()
        _placeAddress = MutableLiveData<String?>()
        _placeCall = MutableLiveData<String?>()
        _placeHomepage = MutableLiveData<String?>()
        _exhibitionInformation = MutableLiveData<String?>()

        _reviewItem = MutableLiveData<UpdateReviewItem?>()
        _deletedReviewPosition = MutableLiveData<Int?>()
        _exhibitionPosterImg = MutableLiveData<String?>()
        _posterUri = MutableLiveData<Uri>()
    }

    //신고
    private var _reportReviewId = MutableLiveData<Int?>()
    val reportReviewId : LiveData<Int?>
        get() = _reportReviewId
    fun setReportReviewId(reviewId:Int?){
        _reportReviewId.value = reviewId
    }
    private var _reportExhibitionId = MutableLiveData<Int?>()
    val reportExhibitionId : LiveData<Int?>
        get() = _reportExhibitionId
    fun setReportExhibitionId(exhibitionId:Int?){
        _reportExhibitionId.value = exhibitionId
    }
    private var _isReport = MutableLiveData(false)
    val isReport : LiveData<Boolean>
        get() = _isReport
    fun setIsReport(check:Boolean){
        _isReport.value = check
    }

    //Managing
    private var _checkListCount = MutableLiveData(false)
    val checkListCount : LiveData<Boolean>
        get() = _checkListCount
    fun setCheckListCount(check:Boolean){
        _checkListCount.value = check
    }

    //댓글 Counting 관리
    fun setReviewItemNumReply(plus:Boolean){
        val item = reviewItem.value
        if(plus){
            item!!.item.numReply++
        }else{
            item!!.item.numReply--
        }
        _reviewItem.value = item
    }

    // Admin Search 개선 관련
    // 공통
    val regexPattern = Regex("[!@#\\\$%^&*(),.?\\\":{}|<>;]")
    private var _searchWord = MutableLiveData<String>()

    val searchWord: LiveData<String>
        get() = _searchWord

    fun setSearchWord(p: String){
        if(p != searchWord.value) _searchWord.value = p
    }

    // 전시회
    var exhibitionHasNextPage = true
    var exhibitionCurrentPage = 0
    private var isLoadingExhibition = false
    private var _exhibitionResultList = MutableLiveData<List<SearchInformationEntity>>()
    val exhibitionResultList: LiveData<List<SearchInformationEntity>>
        get() = _exhibitionResultList

    // 전시장
    var placeHasNextPage = true
    var placeCurrentPage = 0
    private var isLoadingPlace = false
    private var _placeResultList = MutableLiveData<List<SearchPlacesInformationEntity>>()
    val placeResultList: LiveData<List<SearchPlacesInformationEntity>>
        get() = _placeResultList

    // 유저
    var userHasNextPage = true
    var userCurrentPage = 0
    private var isLoadingUser = false
    private var _userResultList = MutableLiveData<List<SearchUserInformationEntity>>()
    val userResultList: LiveData<List<SearchUserInformationEntity>>
        get() = _userResultList

    fun clearList(){
        _exhibitionResultList.value = listOf()
        _placeResultList.value = listOf()
        _userResultList.value = listOf()
    }
    fun searchExhibition(){
        if(!exhibitionHasNextPage) return
        if(isLoadingExhibition) return
        isLoadingExhibition = true

        viewModelScope.launch(Dispatchers.IO){
            val res = ExhibitionRepositoryImpl().searchExhibition(encryptedPrefs.getAT(), searchWord.value.toString(), exhibitionCurrentPage)

            if(res.isSuccessful && res.body()!!.check){
                exhibitionCurrentPage += 1
                exhibitionHasNextPage = res.body()!!.information.hasNextPage

                val tempList = exhibitionResultList.value?.toMutableList() ?: mutableListOf()
                tempList.addAll(res.body()!!.information.data)

                _exhibitionResultList.postValue(tempList)
            }

            isLoadingExhibition = false
        }
    }

    fun searchPlace(){
        if(!placeHasNextPage) return
        if(isLoadingPlace) return
        isLoadingPlace = true

        viewModelScope.launch(Dispatchers.IO){
            val res = PlaceRepositoryImpl().searchPlaces(encryptedPrefs.getAT(), searchWord.value.toString(), placeCurrentPage)

            if(res.isSuccessful && res.body()!!.check){
                placeCurrentPage += 1
                placeHasNextPage = res.body()!!.information.hasNextPage

                val tempList = placeResultList.value?.toMutableList() ?: mutableListOf()
                tempList.addAll(res.body()!!.information.data)

                _placeResultList.postValue(tempList)
            }

            isLoadingPlace = false
        }
    }

    fun searchUser(){
        if(!userHasNextPage) return
        if(isLoadingUser) return
        isLoadingUser = true

        viewModelScope.launch(Dispatchers.IO){
            val res = UserRepositoryImpl().searchUserInfo(encryptedPrefs.getAT(), searchWord.value.toString(), userCurrentPage)

            if(res.isSuccessful && res.body()!!.check){
                userCurrentPage += 1
                userHasNextPage = res.body()!!.information.hasNextPage

                val tempList = userResultList.value?.toMutableList() ?: mutableListOf()
                tempList.addAll(res.body()!!.information.data)

                _userResultList.postValue(tempList)
            }

            isLoadingUser = false
        }
    }

}