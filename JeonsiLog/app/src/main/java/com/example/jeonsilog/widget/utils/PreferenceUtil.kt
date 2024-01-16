package com.example.jeonsilog.widget.utils

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray

class PreferenceUtil(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("AppSettingPrefs", Context.MODE_PRIVATE)

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    fun getSignUpFinished(): Boolean {
        return prefs.getBoolean("signUpFinished", false)
    }

    fun getIsLoginState(): Boolean {
        return prefs.getBoolean("isLoginState", false)
    }

    fun setSignUpFinished(p: Boolean) {
        prefs.edit().putBoolean("signUpFinished", p).apply()
    }

    fun setIsLoginState(p: Boolean) {
        prefs.edit().putBoolean("isLoginState", p).apply()
    }
    fun getRecorList(): ArrayList<String> {
        val itemlist = prefs.getString("serachData", null)
        val resultArr : ArrayList<String> = ArrayList()

        if (itemlist!=null){
            val arrJson = JSONArray(itemlist)
            for(i in 0 until arrJson.length()){
                resultArr.add(arrJson.optString(i))
            }
        }

        //  변환하는 과정
        return resultArr
    }

    fun setRecorList(resultArr: List<String>) {
        val jsonArr = JSONArray()
        for(i in resultArr){
            jsonArr.put(i)
        }
        val result = jsonArr.toString()

        prefs.edit().putString("serachData", result).apply()
    }
    fun setIsAllowNotify(p: Boolean) {
        prefs.edit().putBoolean("isAllowNotify", p).apply()
    }

    fun getIsAllowNotify(): Boolean {
        return prefs.getBoolean("isAllowNotify", false)
    }

}
