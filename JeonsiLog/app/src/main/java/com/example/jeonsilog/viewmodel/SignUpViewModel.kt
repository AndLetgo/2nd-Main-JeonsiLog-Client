package com.example.jeonsilog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel: ViewModel(){

    private var _comment = MutableLiveData<String>(" ")
    val comment: LiveData<String>
        get() = _comment

    fun setComment(comment: String){
        _comment.value = comment
    }
}