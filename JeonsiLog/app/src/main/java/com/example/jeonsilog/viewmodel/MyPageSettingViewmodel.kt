package com.example.jeonsilog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs

class MyPageSettingViewmodel: ViewModel() {
    private var _isRecvActive = MutableLiveData(true)
    private var _isRecvFollowing = MutableLiveData(true)

    val isRecvActive: LiveData<Boolean>
        get() = _isRecvActive

    val isRecvFollowing: LiveData<Boolean>
        get() = _isRecvFollowing

    fun setChecked(){
        _isRecvActive.value = encryptedPrefs.getIsRecvActive()
        _isRecvFollowing.value = encryptedPrefs.getIsRecvFollowing()
    }

    fun setIsRecvActive(p: Boolean){
        _isRecvActive.value = p
        encryptedPrefs.setIsRecvActive(p)
    }

    fun setIsRecvFollowing(p: Boolean){
        _isRecvFollowing.value = p
        encryptedPrefs.setIsRecvFollowing(p)
    }
}