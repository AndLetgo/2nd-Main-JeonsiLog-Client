package com.example.jeonsilog.repository.auth

import android.util.Log
import com.example.jeonsilog.data.remote.RetrofitClient
import com.example.jeonsilog.data.remote.api.AuthApi
import com.example.jeonsilog.data.remote.dto.auth.IsAvailableResponse
import com.example.jeonsilog.data.remote.dto.auth.SignInRequest
import com.example.jeonsilog.data.remote.dto.auth.SignUpRequest
import com.example.jeonsilog.data.remote.dto.auth.TokenRefreshRequest
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.prefs
import retrofit2.Response

class AuthRepositoryImpl: AuthRepository {
    private val service = RetrofitClient.getRetrofit()!!.create(AuthApi::class.java)
    private val tag = this.javaClass.simpleName

    override suspend fun postSignUp(data: SignUpRequest): Boolean {
        val response = service.postSignUp(data)

        return if(response.isSuccessful && response.body()?.check == true){
            Log.d(tag, response.body().toString())
            prefs.setSignUpFinished(true)
            true
        } else {
            Log.e(tag, "회원가입 실패")
            false
        }
    }

    override suspend fun getIsAvailable(nick: String): Response<IsAvailableResponse> {
        val response = service.getIsAvailable(nick)

        return if(response.isSuccessful && response.body()?.check == true){
            response
        } else {
            response
        }
    }

    override suspend fun signIn(data: SignInRequest): Boolean {
        val response = service.signIn(data)

        return if(response.isSuccessful && response.body()?.check == true){
            prefs.setIsLoginState(true)

            encryptedPrefs.setRT(response.body()!!.information.refreshToken)
            encryptedPrefs.setAT(response.body()!!.information.accessToken)
            true
        } else {
            Log.e(tag, "로그인 실패")
            false
        }
    }

    override suspend fun signOut(token: String): Boolean {
        val response = service.signOut("Bearer $token")

        return if(response.isSuccessful && response.body()?.check == true){
            prefs.setIsLoginState(false)
            encryptedPrefs.clearAll()
            true
        } else {
            Log.e(tag, "로그아웃 실패")
            false
        }
    }

    override suspend fun tokenRefresh(token: String): Boolean {
        val response = service.tokenRefresh(TokenRefreshRequest(token))

        return if(response.isSuccessful && response.body()?.check == true){
            encryptedPrefs.setAT(response.body()!!.information.accessToken)
            encryptedPrefs.setRT(response.body()!!.information.refreshToken)
            true
        } else {
            Log.e(tag, "토큰 재발급 실패")
            false
        }
    }

}