package com.example.jeonsilog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyPageSettingViewmodel: ViewModel() {
    private var _isRecvActive = MutableLiveData(true)
    private var _isRecvExhibition = MutableLiveData(true)

    val isRecvActive: LiveData<Boolean>
        get() = _isRecvActive

    val isRecvExhibition: LiveData<Boolean>
        get() = _isRecvExhibition

    fun setChecked(){
        viewModelScope.launch(Dispatchers.IO){
            val response = UserRepositoryImpl().getReception(encryptedPrefs.getAT())
            if(response.isSuccessful && response.body()!!.check){
                launch(Dispatchers.Main) {
                    _isRecvActive.value = response.body()!!.information.isRecvActive
                    _isRecvExhibition.value = response.body()!!.information.isRecvExhibition
                }
            }
        }
    }
}