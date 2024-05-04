package com.example.jeonsilog.widget.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class CryptedPreferenceUtil(context: Context) {

    private val encryptedPrefs: SharedPreferences by lazy {
        val masterKeyAlias = MasterKey
            .Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "EncryptedPrefs",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun clearAll() {
        encryptedPrefs.edit().clear().apply()
    }

    fun setAT(p: String){
        encryptedPrefs.edit().putString("at", p).apply()
    }

    fun setCheckAdmin(p: Boolean){
        encryptedPrefs.edit().putBoolean("checkAdmin", p).apply()
    }

    fun setRT(p: String){
        encryptedPrefs.edit().putString("rt", p).apply()
    }

    fun setNN(p: String){
        encryptedPrefs.edit().putString("nn", p).apply()
    }

    fun setUI(p: Int){
        encryptedPrefs.edit().putInt("ui", p).apply()
    }

    fun setURL(p: String?){
        if(p.isNullOrEmpty()){
            encryptedPrefs.edit().putString("profileImgUrl", "").apply()
        } else {
            encryptedPrefs.edit().putString("profileImgUrl", p).apply()
        }
    }

    fun setNumFollowing(p: Int){
        encryptedPrefs.edit().putInt("numFollowing", p).apply()
    }

    fun setNumFollower(p: Int){
        encryptedPrefs.edit().putInt("numFollower", p).apply()
    }

    fun getAT(): String{
        return encryptedPrefs.getString("at", null).toString()
    }

    fun getCheckAdmin(): Boolean{
        return encryptedPrefs.getBoolean("checkAdmin", false)
    }

    fun getRT(): String {
        return encryptedPrefs.getString("rt", null).toString()
    }

    fun getNN(): String? {
        return encryptedPrefs.getString("nn", null)
    }

    fun getUI(): Int {
        return encryptedPrefs.getInt("ui", -1)
    }

    fun getURL(): String? {
        return encryptedPrefs.getString("profileImgUrl", null)
    }

    fun getNumFollowing(): Int {
        return encryptedPrefs.getInt("numFollowing", 0)
    }

    fun getNumFollower(): Int {
        return encryptedPrefs.getInt("numFollower", 0)
    }

    // 개선 - 레벨 추가
    fun setReviewCount(p: Int){
        encryptedPrefs.edit().putInt("reviewCount", p).apply()
    }

    fun getReviewCount(): Int {
        return encryptedPrefs.getInt("reviewCount", 0)
    }

    fun setUserLevel(p: String){
        encryptedPrefs.edit().putString("userLevel", p).apply()
    }

    fun getUserLevel(): String? {
        return encryptedPrefs.getString("userLevel", null)
    }
}