package com.example.jeonsilog.view.spalshpage

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.jeonsilog.base.BaseActivity
import com.example.jeonsilog.databinding.ActivitySplashBinding
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.view.login.LoginActivity
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient

class SplashActivity : BaseActivity<ActivitySplashBinding>(({ ActivitySplashBinding.inflate(it)})){
    private val TAG = this.javaClass.simpleName

    override fun init() {
        val actionBar = supportActionBar
        actionBar?.hide()

        tokenValidation()
    }

    private fun tokenValidation(){
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        Log.e(TAG, "토큰이 유효하지 않아 사용자 로그인 필요", error)
                    }
                    else {
                        Log.e(TAG, "기타 에러", error)
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }, 3000)
                }
                else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                    Log.d(TAG, "자동 로그인 진행")

                    // 해당 유저 정보가 서버에 있는지 체크 하는 로직 필요

                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }, 3000)
                }
            }
        }
        else {
            Log.d(TAG, "토큰이 존재하지 않음")
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }, 3000)
        }
    }
}