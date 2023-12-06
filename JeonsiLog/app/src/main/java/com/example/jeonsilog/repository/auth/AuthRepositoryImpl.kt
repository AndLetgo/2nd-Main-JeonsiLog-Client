package com.example.jeonsilog.repository.auth

import android.util.Log
import com.example.jeonsilog.data.remote.RetrofitClient
import com.example.jeonsilog.data.remote.api.AuthApi
import com.example.jeonsilog.data.remote.dto.auth.LoginData
import com.example.jeonsilog.data.remote.dto.auth.SignUpData

class AuthRepositoryImpl: AuthRepository {
    private val service = RetrofitClient.getRetrofit()!!.create(AuthApi::class.java)

    override suspend fun postSignUp(data: SignUpData): Boolean {
        val response = service.postSignUp(data)

        return if(response.isSuccessful){
            Log.d("Auth", response.body().toString())
            true
        } else {
            Log.e("Auth", response.errorBody().toString())
            false
        }
    }

    override suspend fun getIsAvailable(nick: String): Boolean {
        val response = service.getIsAvailable(nick)
        Log.d("Auth", "닉네임 중복 체크: ${response.body()?.information?.isAvailable}")

        return if(response.isSuccessful){
            response.body()!!.information.isAvailable
        } else {
            false
        }
    }

    override suspend fun postLogin(data: LoginData) {
    }
}