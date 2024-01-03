package com.example.jeonsilog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExhibitionViewModel:ViewModel() {
    private var _exhibitionName = MutableLiveData<String>()
    val exhibitionName:LiveData<String>
        get() = _exhibitionName

    private var _placeName = MutableLiveData<String>()
    val placeName:LiveData<String>
        get() = _placeName

    private var _placeAddress = MutableLiveData<String>()
    val placeAddress:LiveData<String>
        get() = _placeAddress

    private var _exhibitionDate = MutableLiveData<String>()
    val exhibitionDate:LiveData<String>
        get() = _exhibitionDate

    private var _operatingKeyword = MutableLiveData<String>()
    val operatingKeyword:LiveData<String>
        get() = _operatingKeyword

    private var _priceKeyword = MutableLiveData<String>()
    val priceKeyword:LiveData<String>
        get() = _priceKeyword

    private var _exhibitionDescription = MutableLiveData<String>()
    val exhibitionDescription:LiveData<String>
        get() = _exhibitionDescription

    private var _exhibitionRating = MutableLiveData<String>()
    val exhibitionRating:LiveData<String>
        get() = _exhibitionRating
}