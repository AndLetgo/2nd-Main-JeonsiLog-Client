package com.example.jeonsilog.widget.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

import org.json.JSONArray
import kotlin.math.log

class PreferenceUtil(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("AppSettingPrefs", Context.MODE_PRIVATE)

    fun clearAll() {
        prefs.edit().clear().apply()
    }

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

    fun getIsLoginState(): Boolean {
        return prefs.getBoolean("isLoginState", false)
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

    fun setIsLoginState(p: Boolean) {
        prefs.edit().putBoolean("isLoginState", p).apply()
    }
    fun getRecorList(): ArrayList<String> {
        var itemlist = prefs.getString("serachData", null)
        var resultArr : ArrayList<String> = ArrayList()

        if (itemlist!=null){
            var arrJson = JSONArray(itemlist)
            for(i in 0 until arrJson.length()){
                resultArr.add(arrJson.optString(i))
            }
        }

        //  변환하는 과정
        return resultArr
    }

    fun setRecorList(resultArr: List<String>) {
        var jsonArr = JSONArray()
        for(i in resultArr){
            jsonArr.put(i)
        }
        var result = jsonArr.toString()

        prefs.edit().putString("serachData", result).apply()
    }

}
