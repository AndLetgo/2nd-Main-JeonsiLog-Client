package com.example.jeonsilog.view.spalshpage

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.jeonsilog.base.BaseActivity
import com.example.jeonsilog.data.remote.dto.auth.SignInRequest
import com.example.jeonsilog.databinding.ActivitySplashBinding
import com.example.jeonsilog.repository.auth.AuthRepositoryImpl
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.view.login.LoginActivity
import com.example.jeonsilog.view.login.SignUpActivity
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.prefs
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SplashActivity : BaseActivity<ActivitySplashBinding>(({ ActivitySplashBinding.inflate(it)})) {
    private val tag = this.javaClass.simpleName

    override fun init() {
        val actionBar = supportActionBar
        actionBar?.hide()
        testActivity() // 테스트용 = 바로 메인페이지로 넘어감

//        tokenValidation()
    }

    private fun tokenValidation() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        Log.e(tag, "토큰이 유효하지 않아 사용자 로그인 필요", error)
                    } else {
                        Log.e(tag, "기타 에러", error)
                    }
                    moveActivity(LoginActivity())
                } else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                    CoroutineScope(Dispatchers.IO).launch{
                        Log.d(tag, "자동 로그인 진행")

                        if(prefs.getSignUpFinished()){   // SignUp Page 완료한 경우
                            if(prefs.getIsLoginState()){ // 서버 로그인 상태인 경우

                                val flag = UserRepositoryImpl().getMyInfo(encryptedPrefs.getAT()!!) // 토큰 유효성 검사
                                if(flag){ // 토큰이 유효한 경우 메인 페이지로 이동
                                    Log.d(tag, "서버 토큰 유효함")
                                    CoroutineScope(Dispatchers.Main).launch {
                                        moveActivity(MainActivity())
                                    }
                                } else {  // 토큰이 유효하지 않아 재발급 요청 후 재검증
                                    Log.e(tag, "서버 토큰 유효하지 않음")

                                    if(AuthRepositoryImpl().tokenRefresh(encryptedPrefs.getRT()!!)){
                                        CoroutineScope(Dispatchers.Main).launch {
                                            moveActivity(SplashActivity())
                                        }
                                    } else {
                                        UserApiClient.instance.logout { error ->
                                            Log.e(tag, "refreshToken 만료 -> 재로그인 필요함")
                                            if(error != null){
                                                Log.e(tag, "로그아웃 실패")
                                            } else {
                                                Log.d(tag, "로그아웃 진행")
                                                prefs.setIsLoginState(false)
                                                CoroutineScope(Dispatchers.Main).launch {
                                                    moveActivity(SplashActivity())
                                                }
                                            }
                                        }
                                    }
                                }
                            } else { // 서버 비로그인 상태 -> 로그인 진행
                                val data = getUserDataFromKakao()

                                if(AuthRepositoryImpl().signIn(data!!)){
                                    Log.d(tag, "서버 로그인 성공")
                                    CoroutineScope(Dispatchers.Main).launch {
                                        moveActivity(MainActivity())
                                    }
                                } else {
                                    Log.d(tag, "서버 로그인 실패")
                                }
                            }
                        } else {
                            // SignUp Page을 진행하지 않은 경우 또는 다른 기기에서 접속하는 경우도 포함됨
                            val data = getUserDataFromKakao()
                            if(AuthRepositoryImpl().signIn(data!!)){
                                Log.d(tag, "회원 정보 존재함 -> 로그인 진행")
                                prefs.setSignUpFinished(true)
                                CoroutineScope(Dispatchers.Main).launch {
                                    moveActivity(SplashActivity())
                                }
                            } else {
                                Log.d(tag, "SignUp Page 진행 필요")
                                CoroutineScope(Dispatchers.Main).launch {
                                    moveActivity(SignUpActivity())
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Log.d(tag, "토큰이 존재하지 않음")
            moveActivity(LoginActivity())
        }
    }
    private suspend fun getUserDataFromKakao(): SignInRequest? {
        return suspendCoroutine { continuation ->
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e(tag, error.message.toString())
                    continuation.resume(null)
                } else {
                    if (user != null) {
//                        val data = SignInRequest(
//                            providerId = user.id.toString(),
//                            email = user.kakaoAccount!!.email.toString(),
//                        )
                        val data = SignInRequest("test3@gmail.com", "testId3")
                        continuation.resume(data)
                    } else {
                        continuation.resume(null)
                    }
                }
            }
        }
    }

    private fun moveActivity(p: Any){
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, p.javaClass)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }, 3000)
    }

    private fun testActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}