package com.example.jeonsilog.data.remote

import android.util.Log
import com.example.jeonsilog.BuildConfig
import com.example.jeonsilog.data.remote.dto.auth.TokenRefreshResponse
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFinish
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.prefs
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import org.json.JSONObject

class TokenRefreshInterceptor : Authenticator {
    private val tag = this.javaClass.simpleName

    override fun authenticate(route: Route?, response: Response): Request? {
        val originRequest = response.request

        Log.d(tag, "authenticate 진입")

        if (originRequest.header("Authorization").isNullOrEmpty()) {
            Log.d(tag, "요청 헤더에 토큰이 없음")
            return null
        }

        if (!prefs.getIsLoginState()) {
            Log.d(tag, "로그인 상태가 아님")
            return null
        }

        val refreshToken = runBlocking {
            encryptedPrefs.getRT()
        }

        val jsonObject = JSONObject().apply {
            put("refreshToken", refreshToken)
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()

        val refreshRequest = Request.Builder()
            .url("${BuildConfig.BASE_URL}auth/refresh")
            .post(jsonObject.toString().toRequestBody(mediaType))
            .build()

        try {
            val refreshResponse = OkHttpClient().newCall(refreshRequest).execute()
            val refreshResponseJson =
                Gson().fromJson(refreshResponse.body?.string(), TokenRefreshResponse::class.java)

            if (refreshResponse.isSuccessful && refreshResponseJson.check) {
                // 재발급 성공
                Log.d(tag, "Refresh Token 재발급 성공")
                encryptedPrefs.setAT(refreshResponseJson.information.accessToken)
                encryptedPrefs.setRT(refreshResponseJson.information.refreshToken)

                return originRequest.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer ${encryptedPrefs.getAT()}")
                    .build()

            } else {
                // 재발급 실패 - refreshToken 만료
                Log.d(tag, "Refresh Token 재발급 실패 - 만료됨")

                CoroutineScope(Dispatchers.Main).launch {
                    encryptedPrefs.clearAll()
                    prefs.setIsLoginState(false)
                    isFinish.value = true
                }
            }
        } catch (e: Exception) {
            // 네트워크 예외 처리
            Log.e(tag, "Network error: ${e.message}")

            CoroutineScope(Dispatchers.Main).launch {
                encryptedPrefs.clearAll()
                prefs.setIsLoginState(false)
                isFinish.value = true
            }
        }

        return null
    }
}
