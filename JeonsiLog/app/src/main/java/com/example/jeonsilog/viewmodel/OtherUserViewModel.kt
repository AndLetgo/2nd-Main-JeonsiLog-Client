package com.example.jeonsilog.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jeonsilog.R
import com.example.jeonsilog.data.remote.dto.user.MyInfoInformation
import com.example.jeonsilog.repository.follow.FollowRepositoryImpl
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class OtherUserViewModel: ViewModel() {
    private var _userId = MutableLiveData<Int>()
    val userId: LiveData<Int>
        get() = _userId

    private var _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

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

    private var _flag = MutableLiveData(false)
    val flag: LiveData<Boolean>
        get() = _flag

    fun setTitle(context: Context){
        _title.value = context.getString(R.string.other_nick_title, nick.value)

    }

    fun getOtherUserInfo(otherUserId: Int){
        Log.d("vm", "getOtherUserInfo")

        try{
            val userData: MyInfoInformation
            runBlocking(Dispatchers.IO) {
                val response = UserRepositoryImpl().getOtherInfo(encryptedPrefs.getAT(), otherUserId)
                userData = if(response.isSuccessful && response.body()!!.check){
                    response.body()!!.information
                } else {
                    MyInfoInformation(0, "알 수 없음", "null", 0, 0)
                }
            }

            runBlocking(Dispatchers.IO) {
                var page = 0

                while(true){
                    val response2 = FollowRepositoryImpl().getMyFollowing(encryptedPrefs.getAT(), page)
                    if (response2.isSuccessful && response2.body()!!.check) {
                        val userList = response2.body()!!.information.data.listIterator()
                        while (userList.hasNext()) {
                            if (userList.next().followUserId == otherUserId) {
                                viewModelScope.launch(Dispatchers.Main) {
                                    _flag.value = true
                                }
                            }
                        }
                    } else {
                        break
                    }

                    page++
                }

            }


            viewModelScope.launch(Dispatchers.Main){
                _userId.value = userData.userId
                _nick.value = userData.nickname
                _profileImg.value = userData.profileImgUrl
                _follower.value = userData.numFollower.toString()
                _following.value = userData.numFollowing.toString()
            }
        } catch (e: Exception) {throw IllegalArgumentException("타유저 정보 요청 실패")}
    }

    fun changeFlag(){
        if(flag.value!!){
            runBlocking(Dispatchers.IO){
                val response = FollowRepositoryImpl().deleteFollow(encryptedPrefs.getAT(), userId.value!!)

                viewModelScope.launch(Dispatchers.Main) {
                    if (response.isSuccessful && response.body()!!.check) {
                        _flag.value = false
                    }
                }
            }
        } else {
            runBlocking(Dispatchers.IO){
                val response = FollowRepositoryImpl().postFollow(encryptedPrefs.getAT(), userId.value!!)

                viewModelScope.launch(Dispatchers.Main) {
                    if (response.isSuccessful && response.body()!!.check) {
                        _flag.value = true
                    }
                }
            }
        }
        getOtherUserInfo(userId.value!!)
    }
}