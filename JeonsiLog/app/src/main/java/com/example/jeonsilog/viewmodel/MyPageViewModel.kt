package com.example.jeonsilog.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MyPageViewModel: ViewModel() {
    private var _nick = MutableLiveData<String>()
    val nick: LiveData<String>
        get() = _nick

    private var _profileImg = MutableLiveData<String>()
    val profileImg: LiveData<String>
        get() = _profileImg

    private var _following = MutableLiveData<String>()
    val following: LiveData<String>
        get() = _following

    private var _follower = MutableLiveData<String>()
    val follower: LiveData<String>
        get() = _follower

    fun setNick(nick: String){
        _nick.value = nick
    }

    fun setProfileImg(url: String){
        _profileImg.value = url
    }

    private var _flag = MutableLiveData<Boolean>(false)
    val flag: LiveData<Boolean>
        get() = _flag

    fun getMyInfo(){
        Log.d("vm", "getMyInfo")
        runBlocking(Dispatchers.IO) {
            UserRepositoryImpl().getMyInfo(encryptedPrefs.getAT())
        }
        setInfo()
    }

    private fun setInfo(){
        Log.d("vm", "setInfo")
        _nick.value = encryptedPrefs.getNN()
        _profileImg.value = encryptedPrefs.getURL()
        _following.value = encryptedPrefs.getNumFollowing().toString()
        _follower.value = encryptedPrefs.getNumFollower().toString()

    }
}