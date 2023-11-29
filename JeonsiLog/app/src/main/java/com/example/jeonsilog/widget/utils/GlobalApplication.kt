package com.example.jeonsilog.widget.utils

import android.app.Application


class GlobalApplication: Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }

}