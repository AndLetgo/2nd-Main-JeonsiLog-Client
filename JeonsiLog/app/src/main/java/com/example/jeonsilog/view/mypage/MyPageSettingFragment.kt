package com.example.jeonsilog.view.mypage

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.jeonsilog.BuildConfig
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentMyPageSettingBinding
import com.example.jeonsilog.repository.auth.AuthRepositoryImpl
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.view.spalshpage.SplashActivity
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MyPageSettingFragment: BaseFragment<FragmentMyPageSettingBinding>(R.layout.fragment_my_page_setting) {
    override fun init() {
        try{
            (activity as MainActivity).setStateBn(false)
        }catch (e:ClassCastException){

        }

        binding.switchMypageSettingFollowing.isChecked = encryptedPrefs.getIsRecvFollowing()
        binding.switchMypageSettingActivity.isChecked = encryptedPrefs.getIsRecvActive()
        binding.tvMypageSettingVersion.text = getString(R.string.setting_version, BuildConfig.VERSION_NAME)

        binding.switchMypageSettingFollowing.setOnCheckedChangeListener { _, _ ->
            runBlocking(Dispatchers.IO){
                val response = UserRepositoryImpl().patchAlarmFollowing(encryptedPrefs.getAT())
                if(response.isSuccessful && response.body()!!.check){
                    encryptedPrefs.setIsRecvFollowing(response.body()!!.information.recvFollowing)
                }
            }
            binding.switchMypageSettingFollowing.isChecked = encryptedPrefs.getIsRecvFollowing()
        }

        binding.switchMypageSettingActivity.setOnCheckedChangeListener { _, _ ->
            runBlocking(Dispatchers.IO){
                val response = UserRepositoryImpl().patchAlarmActive(encryptedPrefs.getAT())
                if(response.isSuccessful && response.body()!!.check){
                    encryptedPrefs.setIsRecvActive(response.body()!!.information.recvActive)
                }
            }

            binding.switchMypageSettingActivity.isChecked = encryptedPrefs.getIsRecvActive()
        }

        binding.ibMypageSettingGoWeb.setOnClickListener {
            val webpage = Uri.parse("https://sites.google.com/view/jeonsilog/%ED%99%88")
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            if(intent.resolveActivity(requireActivity().packageManager) != null){
                startActivity(intent)
            }
        }

        binding.tvMypageSettingLogout.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if(error != null){
                    Log.e("LOGIN", error.message.toString())
                }
                CoroutineScope(Dispatchers.IO).launch{
                    if(AuthRepositoryImpl().signOut(encryptedPrefs.getAT())){
                        launch(Dispatchers.Main) {
                            val intent = Intent(requireContext(), SplashActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        launch(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "로그아웃 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.tvMypageSettingUnlink.setOnClickListener {
            showCustomDialog()
        }

    }

    private fun showCustomDialog() {
        val customDialogFragment = MyPageUnLinkDialog()
        customDialogFragment.show(parentFragmentManager, "MyPageUnLinkDialog")
    }
}