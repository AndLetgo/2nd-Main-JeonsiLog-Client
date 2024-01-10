package com.example.jeonsilog.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jeonsilog.R
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

    fun nickAvailableCheck(nick: String, context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            val response = AuthRepositoryImpl().getIsAvailable(nick)
            if (response.isSuccessful && response.body()!!.check) {
                val result = response.body()!!.information

                launch(Dispatchers.Main) {
                    if (!result.isDuplicate && !result.isForbidden) {
                        onBtnFlagChange(true)
                    } else if (result.isDuplicate) {
                        setComment(context.getString(R.string.login_nick_check_duplicate))
                    } else {
                        setComment(context.getString(R.string.login_nick_check_forbidden))
                    }
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
}