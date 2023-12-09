package com.example.jeonsilog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.w3c.dom.Text

class HomeViewModel:ViewModel(){
    private var _id = MutableLiveData<Long>()
    val id: LiveData<Long>
        get() = _id

    private var _placedId = MutableLiveData<Long>()
    val placedId: LiveData<Long>
        get() = _placedId

    private var _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private var _operatingKeyword = MutableLiveData<String>()
    val operatingKeyword: LiveData<String>
        get() = _operatingKeyword

    private var _priceKeyword = MutableLiveData<String>()
    val priceKeyword: LiveData<String>
        get()=_priceKeyword

    private var _price = MutableLiveData<String>()
    val price: LiveData<String>
        get() = _price

    private var _startDate = MutableLiveData<String>()
    val startDate: LiveData<String>
        get() = _startDate

    private var _endDate = MutableLiveData<String>()
    val endDate: LiveData<String>
        get() = _endDate

    private var _information = MutableLiveData<Text>()
    val information : LiveData<Text>
        get() = _information

    private var _rate = MutableLiveData<Double>()
    val rate: LiveData<Double>
        get() = _rate
}
