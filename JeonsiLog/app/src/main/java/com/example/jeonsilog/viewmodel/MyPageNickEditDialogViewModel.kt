package com.example.jeonsilog.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyPageNickEditDialogViewModel: ViewModel() {
    private var _comment = MutableLiveData("")
    private var _isNickFocused = MutableLiveData(false)
    private var _flag = MutableLiveData(false)

    val comment: LiveData<String>
        get() = _comment

    fun setComment(comment: String){
        _comment.value = comment
    }

    val isNickFocused: LiveData<Boolean>
        get() = _isNickFocused

    fun onNickFocusChange(v:View?, hasFocus: Boolean) {
        _isNickFocused.value = hasFocus
    }

    val flag: LiveData<Boolean>
        get() = _flag

    fun setFlag(p: Boolean){
        _flag.value = p
    }
}