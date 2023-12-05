package com.example.jeonsilog.view

import android.content.Intent
import android.util.Log
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentMyPageBinding
import com.example.jeonsilog.view.spalshpage.SplashActivity
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.prefs
import com.kakao.sdk.user.UserApiClient

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    override fun init() {
        binding.btnLogout.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if(error != null){
                    Log.e("LOGIN", error.message.toString())
                }
                val intent = Intent(this.context, SplashActivity::class.java)
                startActivity(intent)
            }
        }

        binding.btnUnlink.setOnClickListener {
            UserApiClient.instance.unlink { error ->
                if(error != null){
                    Log.e("LOGIN", error.message.toString())
                }
                prefs.setSignUpFinished(false)
                val intent = Intent(this.context, SplashActivity::class.java)
                startActivity(intent)
            }
        }
    }

}