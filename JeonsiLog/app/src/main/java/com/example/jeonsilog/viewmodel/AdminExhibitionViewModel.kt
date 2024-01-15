package com.example.jeonsilog.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdminExhibitionViewModel:ViewModel() {
    private var _exhibitionName = MutableLiveData<String>()
    val exhibitionName: LiveData<String>
        get()= _exhibitionName
    fun setExhibitionName(name:String){
        _exhibitionName.value = name
    }

    private var _placeName = MutableLiveData<String>()
    val placeName: LiveData<String>
        get()= _placeName
    fun setPlaceName(name:String){
        _placeName.value = name
    }

    private var _placeAddress = MutableLiveData<String>()
    val placeAddress : LiveData<String>
        get() = _placeAddress
    fun setAddress(address: String){
        _placeAddress.value = address
    }

    private var _placeCall = MutableLiveData<String>()
    val placeCall : LiveData<String>
        get() = _placeCall
    fun setPlaceCall(call:String){
        _placeCall.value = call
    }

    private var _placeHomepage = MutableLiveData<String>()
    val placeHomepage : LiveData<String>
        get() = _placeHomepage
    fun setPlaceHomepage(homepage:String){
        _placeHomepage.value = homepage
    }

    private var _exhibitionInformation = MutableLiveData<String>()
    val exhibitionInformation : LiveData<String>
        get() = _exhibitionInformation
    fun setExhibitionInformation(information:String){
        _exhibitionInformation.value = information
    }
}