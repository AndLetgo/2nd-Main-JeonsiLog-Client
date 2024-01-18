package com.example.jeonsilog.view.mypage

import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentMyPageListBinding
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isRefresh
import com.google.android.material.tabs.TabLayoutMediator

class MyPageListFragment(private val startTab: Int): BaseFragment<FragmentMyPageListBinding>(R.layout.fragment_my_page_list) {

    override fun init() {
        try{
            (activity as MainActivity).setStateBn(false, "user")
        }catch (e:ClassCastException){

        }

        isRefresh.observe(this){
            if(it){
                (activity as MainActivity).refreshFragment(MyPageListFragment(startTab))
                isRefresh.value = false
            }
        }

        binding.tvMypageListNick.text = encryptedPrefs.getNN()

        binding.vpMypageList.adapter = MyPageListVpAdapter(this.requireActivity())

        val tabTextList = listOf(getString(R.string.mypage_follower), getString(R.string.mypage_following))

        TabLayoutMediator(binding.tlMypageList, binding.vpMypageList){ tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()

        binding.vpMypageList.post {
            binding.vpMypageList.currentItem = startTab
        }
    }
}