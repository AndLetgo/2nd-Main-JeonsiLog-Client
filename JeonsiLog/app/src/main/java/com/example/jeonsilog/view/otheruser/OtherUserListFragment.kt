package com.example.jeonsilog.view.otheruser

import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentOtherUserListBinding
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.widget.utils.GlobalApplication

import com.google.android.material.tabs.TabLayoutMediator

class OtherUserListFragment(private val startTab: Int, private val otherUserId: Int, private val otherUserNick: String): BaseFragment<FragmentOtherUserListBinding>(R.layout.fragment_other_user_list) {
    override fun init() {
        try{
            (activity as MainActivity).setStateBn(false, "user")
        }catch (e:ClassCastException){

        }


        GlobalApplication.isRefresh.observe(this){
            if(it){
                (activity as MainActivity).refreshFragment(OtherUserListFragment(startTab, otherUserId, otherUserNick))
                GlobalApplication.isRefresh.value = false
            }
        }


        binding.tvOtherUserListNick.text = otherUserNick

        binding.vpOtherUserList.adapter = OtherUserListVpAdapter(this.requireActivity(), otherUserId)

        val tabTextList = listOf(getString(R.string.mypage_follower), getString(R.string.mypage_following))

        TabLayoutMediator(binding.tlOtherUserList, binding.vpOtherUserList){ tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()

        binding.vpOtherUserList.post {
            binding.vpOtherUserList.currentItem = startTab
        }
    }

}