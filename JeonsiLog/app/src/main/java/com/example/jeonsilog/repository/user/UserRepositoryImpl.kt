package com.example.jeonsilog.repository.user

import android.util.Log
import com.example.jeonsilog.data.remote.RetrofitClient
import com.example.jeonsilog.data.remote.api.UserApi
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.prefs


class UserRepositoryImpl: UserRepository {
    private val service = RetrofitClient.getRetrofit()!!.create(UserApi::class.java)
    private val tag = this.javaClass.simpleName

    override suspend fun getMyInfo(token: String): Boolean {
        val response = service.getMyInfo("Bearer $token")

        return if(response.isSuccessful && response.body()?.check == true){
            encryptedPrefs.setNN(response.body()!!.information.nickname)
            encryptedPrefs.setUI(response.body()!!.information.userId)
            encryptedPrefs.setURL(response.body()!!.information.profileImgUrl)
            true
        } else {
            Log.e(tag, "내 정보 불러오기 실패")
            false
        }
    }

    override suspend fun doUnLink(token: String): Boolean {
        val response = service.doUnLink("Bearer $token")

        return if(response.isSuccessful && response.body()?.check == true){
            encryptedPrefs.clearAll()
            prefs.clearAll()
            true
        } else {
            Log.e(tag, "회원탈퇴 실패")
            false
        }
    }
}