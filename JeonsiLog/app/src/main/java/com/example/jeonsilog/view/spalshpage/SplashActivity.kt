package com.example.jeonsilog.view.spalshpage

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.jeonsilog.base.BaseActivity
import com.example.jeonsilog.databinding.ActivitySplashBinding
import com.example.jeonsilog.view.MainActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(({ ActivitySplashBinding.inflate(it)})){
    override fun init() {
        val actionBar = supportActionBar
        actionBar?.hide()

        // 토큰 유효성 검사
        val result = tokenValidation()

        if(result){
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }, 5000)
        }
        else {
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
        }
    }

    private fun tokenValidation(): Boolean{
        return true
    }
}