package com.example.jeonsilog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExhibitionPlaceViewModel:ViewModel() {
    private var _exhibitoinName = MutableLiveData<String>()
    val exhibitionName: LiveData<String>
        get() = _exhibitoinName

    private var _date = MutableLiveData<String>()
    val date: LiveData<String>
        get() = _date

    private var _imageUrl = MutableLiveData<String>()
    val imageUrl:LiveData<String>
        get() = _imageUrl

    private var _operatingKeyword = MutableLiveData<String>()
    val operatingKeyword:LiveData<String>
        get() = _operatingKeyword

    private var _priceKeyword = MutableLiveData<String>()
    val priceKeyword: LiveData<String>
        get() = _priceKeyword
}