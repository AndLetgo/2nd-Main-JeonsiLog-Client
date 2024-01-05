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
    private var _btnFlag = MutableLiveData(false)
    private var _checkableFlag = MutableLiveData(false)
    private var _etNick = MutableLiveData("")
    private var _profileImagePath = MutableLiveData("")
    private var _tosIsCheckedTos = MutableLiveData(false)
    private var _tosIsCheckedPermissionPhoto = MutableLiveData(false)
    private var _tosIsCheckedAll = MutableLiveData(false)
    private var _firstRequest = MutableLiveData(0)

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
            launch(Dispatchers.Main) {
                if(AuthRepositoryImpl().getIsAvailable(nick)){
                    onBtnFlagChange(true)
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

    val tosIsCheckedTos: LiveData<Boolean>
        get() = _tosIsCheckedTos

    val tosIsCheckedPermissionPhoto: LiveData<Boolean>
        get() = _tosIsCheckedPermissionPhoto

    val tosIsCheckedAll: LiveData<Boolean>
        get() = _tosIsCheckedAll

    fun changeAll(p: Boolean){
        _tosIsCheckedTos.value = p
        _tosIsCheckedPermissionPhoto.value = p
        _tosIsCheckedAll.value = p
    }

    fun changeTosTos(p: Boolean){
        _tosIsCheckedTos.value = p

        _tosIsCheckedAll.value = tosIsCheckedTos.value!! && tosIsCheckedPermissionPhoto.value!!
    }

    fun changeTosPhoto(p: Boolean){
        _tosIsCheckedPermissionPhoto.value = p

        _tosIsCheckedAll.value = tosIsCheckedTos.value!! && tosIsCheckedPermissionPhoto.value!!
    }

    val firstRequest: LiveData<Int>
        get() = _firstRequest

    fun changeFirstRequest(p: Int){
        _firstRequest.value = p
    }

    private var _updateFlag = MutableLiveData(false)
    val updateFlag: LiveData<Boolean>
        get() = _updateFlag

    fun setUpdateFlag(p: Boolean){
        _updateFlag.value = p
    }
}