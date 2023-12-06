package com.example.jeonsilog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

//    private var _
}
