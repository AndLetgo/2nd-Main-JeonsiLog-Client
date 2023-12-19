package com.example.jeonsilog.view.mypage

import android.content.Intent
import android.util.Log
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentMyPageBinding
import com.example.jeonsilog.repository.auth.AuthRepositoryImpl
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.view.spalshpage.SplashActivity
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    override fun init() {
        val tabTextList = listOf(getString(R.string.mypage_my_rating), getString(R.string.mypage_my_review), getString(R.string.mypage_favorites))

        binding.vpMypage.adapter = MyPageVpAdapter(this.requireActivity())

        TabLayoutMediator(binding.tlMypage, binding.vpMypage){ tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()
    }

}







//binding.btnLogout.setOnClickListener {
//    UserApiClient.instance.logout { error ->
//        if(error != null){
//            Log.e("LOGIN", error.message.toString())
//        }
//        CoroutineScope(Dispatchers.IO).launch{
//            AuthRepositoryImpl().signOut(encryptedPrefs.getAT().toString())
//        }
//        CoroutineScope(Dispatchers.Main).launch {
//            val intent = Intent(requireContext(), SplashActivity::class.java)
//            startActivity(intent)
//        }
//    }
//}
//
//binding.btnUnlink.setOnClickListener {
//    UserApiClient.instance.unlink { error ->
//        if(error != null){
//            Log.e("LOGIN", error.message.toString())
//        }
//        CoroutineScope(Dispatchers.IO).launch{
//            UserRepositoryImpl().doUnLink(encryptedPrefs.getAT().toString())
//        }
//        CoroutineScope(Dispatchers.Main).launch {
//            val intent = Intent(requireContext(), SplashActivity::class.java)
//            startActivity(intent)
//        }
//    }
//}