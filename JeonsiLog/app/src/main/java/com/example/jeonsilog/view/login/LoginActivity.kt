package com.example.jeonsilog.view.login

import android.content.Intent
import android.util.Log
import com.example.jeonsilog.base.BaseActivity
import com.example.jeonsilog.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class LoginActivity: BaseActivity<ActivityLoginBinding>({ActivityLoginBinding.inflate(it)}) {
    private val TAG = this.javaClass.simpleName

    // Base Actvity에 추가 논의
//    private val callback = object : OnBackPressedCallback(true) {
//        override fun handleOnBackPressed() {
//            // 뒤로 버튼 이벤트 처리
//            Log.e(TAG, "뒤로가기 클릭")
//        }
//    }
//    this.onBackPressedDispatcher.addCallback(this, callback)


    override fun init() {
        val actionBar = supportActionBar
        actionBar?.hide()

        binding.btnLogin.setOnClickListener{
            doLogin()
        }
    }

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "로그인 실패 $error")
        } else if (token != null) {
            Log.e(TAG, "로그인 성공 ${token.accessToken}")
            nextActivity()
        }
    }

    private fun doLogin(){
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")

                    UserApiClient.instance.me { user, error ->
                        if(error != null){
                            Log.e(TAG, "사용자 정보 요청 실패", error)
                        }
                        else if(user != null){
                            nextActivity()
                        }
                    }
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun nextActivity(){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }
}