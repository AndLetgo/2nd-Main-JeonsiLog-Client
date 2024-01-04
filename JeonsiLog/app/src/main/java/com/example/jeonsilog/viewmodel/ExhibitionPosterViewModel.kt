package com.example.jeonsilog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExhibitionPosterViewModel:ViewModel() {
    private var _count = MutableLiveData("1")
    val count: LiveData<String>
        get() = _count
    fun setCount(count: String){
        _count.value = count
    }

    private var _maxCount = MutableLiveData<String>()
    val maxCount: LiveData<String>
        get() = _maxCount
    fun setMaxCount(maxCount:String){
        _maxCount.value = maxCount
    }
}