package com.example.jeonsilog.widget.utils

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.jeonsilog.BuildConfig
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility

class GlobalApplication: Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
        lateinit var encryptedPrefs: CryptedPreferenceUtil
        var networkState = MutableLiveData(true)
        lateinit var globalContext: GlobalApplication
            private set

        var isFinish = MutableLiveData(false)
        var isFollowerUpdate = MutableLiveData(false)
        var isFollowingUpdate = MutableLiveData(false)
        var isRefresh = MutableLiveData(false)

        var exhibitionId: Int = 0
        var newPlaceId: Int = 0
        var newPlaceName: String = ""
        var extraActivityReference: Int = 0
    }

    override fun onCreate() {
        super.onCreate()
        prefs = PreferenceUtil(applicationContext)
        encryptedPrefs = CryptedPreferenceUtil(applicationContext)
        globalContext = this

        getKeyHash()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
    }

    private fun getKeyHash(){
        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)
    }

    fun exitApp(){
        android.os.Process.killProcess(android.os.Process.myPid())
    }
}