package com.example.jeonsilog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExhibitionWritingViewModel: ViewModel() {
    private var _writingCount = MutableLiveData("0")
    val writingCount : LiveData<String>
        get() = _writingCount
    fun setWritingCount(writingCount:String){
        _writingCount.value = writingCount
    }

    private var _checkCount = MutableLiveData(false)
    val checkCount: LiveData<Boolean>
        get() = _checkCount
    fun setCheckCount(check:Boolean){
        _checkCount.value = check
    }
}