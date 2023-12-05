package com.example.jeonsilog.widget.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("AppSettingPrefs", Context.MODE_PRIVATE)

    fun getOnBoardingMode(): Boolean {
        return prefs.getBoolean("onBoardingMode", true)
    }

    fun getFollowingMode(): Boolean {
        return prefs.getBoolean("followingMode", true)
    }

    fun getMyActivityMode(): Boolean {
        return prefs.getBoolean("myActivityMode", true)
    }

    fun getSignUpFinished(): Boolean {
        return prefs.getBoolean("signUpFinished", false)
    }

    fun setOnBoardingMode(p: Boolean) {
        prefs.edit().putBoolean("onBoardingMode", p).apply()
    }

    fun setFollowingMode(p: Boolean) {
        prefs.edit().putBoolean("followingMode", p).apply()
    }

    fun setMyActivityMode(p: Boolean) {
        prefs.edit().putBoolean("myActivityMode", p).apply()
    }

    fun setSignUpFinished(p: Boolean) {
        prefs.edit().putBoolean("signUpFinished", p).apply()
    }





}