package com.example.jeonsilog.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jeonsilog.repository.auth.AuthRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel: ViewModel(){
    private var _comment = MutableLiveData("")
    private var _isNickFocused = MutableLiveData(false)
    private var _btnFlag = MutableLiveData(true)
    private var _checkableFlag = MutableLiveData(false)
    private var _etNick = MutableLiveData("")
    private var _profileImagePath = MutableLiveData("")

    val comment: LiveData<String>
        get() = _comment

    fun setComment(comment: String){
        _comment.value = comment
    }

    val isNickFocused: LiveData<Boolean>
        get() = _isNickFocused

    fun onNickFocusChange(v: View?, hasFocus: Boolean) {
        _isNickFocused.value = hasFocus
    }

    val btnFlag: LiveData<Boolean>
        get() = _btnFlag

    fun onBtnFlagChange(state: Boolean){
        _btnFlag.value = state
    }

    fun duplicateCheck(nick: String, comment: String){
        viewModelScope.launch(Dispatchers.IO){
            val flag = AuthRepositoryImpl().getIsAvailable(nick)

            launch(Dispatchers.Main) {
                if(flag){
                    onBtnFlagChange(false)
                } else {
                    setComment(comment)
                }
            }
        }
    }

    val checkableFlag: LiveData<Boolean>
        get() = _checkableFlag

    fun onCheckableFlagChange(state: Boolean){
        _checkableFlag.value = state
    }

    val etNick: LiveData<String>
        get() = _etNick

    val profileImagePath: LiveData<String>
        get() = _profileImagePath

    fun setProfileUrl(path: String){
        _profileImagePath.value = path
    }
}