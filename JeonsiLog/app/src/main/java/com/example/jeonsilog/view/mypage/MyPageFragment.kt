package com.example.jeonsilog.view.mypage

import android.util.Log
import androidx.fragment.app.viewModels
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentMyPageBinding
import com.example.jeonsilog.viewmodel.MyPageViewModel
import com.example.jeonsilog.widget.utils.GlideApp
import com.google.android.material.tabs.TabLayoutMediator

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: MyPageViewModel by viewModels()

    override fun init() {
        viewModel.getMyInfo()
        binding.vm = viewModel
        binding.lifecycleOwner = this

        viewModel.profileImg.observe(this){
            GlideApp.with(this)
                .load(it)
                .centerCrop()
                .circleCrop()
                .into(binding.ivMypageProfile)
        }

        val tabTextList = listOf(getString(R.string.mypage_my_rating), getString(R.string.mypage_my_review), getString(R.string.mypage_favorites))

        binding.vpMypage.adapter = MyPageVpAdapter(this.requireActivity())

        TabLayoutMediator(binding.tlMypage, binding.vpMypage){ tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()

        binding.ibMypageNickEdit.setOnClickListener {
            Log.d("TAG", "editNick")
            showCustomDialog()
        }

        binding.ibMypageProfileEdit.setOnClickListener {
            Log.d("TAG", "editProfile")
        }

        binding.ibMypageSetting.setOnClickListener {
            Log.d("TAG", "moveSettingPage")
        }
    }
    private fun showCustomDialog() {
        val customDialogFragment = MyPageNickEditDialog(viewModel)
        customDialogFragment.show(parentFragmentManager, "custom_dialog")
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