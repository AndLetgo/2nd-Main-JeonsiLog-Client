package com.example.jeonsilog.view.login

import android.content.Intent
import com.example.jeonsilog.base.BaseActivity
import com.example.jeonsilog.databinding.ActivityLoginBinding

class LoginActivity: BaseActivity<ActivityLoginBinding>({ActivityLoginBinding.inflate(it)}) {
    override fun init() {
        val actionBar = supportActionBar
        actionBar?.hide()

        binding.btnLogin.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}