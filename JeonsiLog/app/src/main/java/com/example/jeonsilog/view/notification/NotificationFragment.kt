package com.example.jeonsilog.view.notification

import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentNotificationBinding
import com.example.jeonsilog.view.MainActivity
import com.google.android.material.tabs.TabLayoutMediator

class NotificationFragment : BaseFragment<FragmentNotificationBinding>(
    R.layout.fragment_notification) {

    override fun init() {
        (context as MainActivity).setStateBn(true, "user")

        val tabTextList = listOf(getString(R.string.noti_activity_tab), getString(R.string.noti_exhibition_tab))

        binding.vpNoti.adapter = NotiVpAdapter(this.requireActivity())

        TabLayoutMediator(binding.tlNoti, binding.vpNoti){ tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()
    }
}