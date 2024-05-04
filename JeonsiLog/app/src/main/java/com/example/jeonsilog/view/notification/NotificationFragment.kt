package com.example.jeonsilog.view.notification

import android.os.Handler
import android.os.Looper
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentNotificationBinding
import com.example.jeonsilog.view.MainActivity
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayoutMediator

class NotificationFragment(private val action:String) : BaseFragment<FragmentNotificationBinding>(
    R.layout.fragment_notification) {

    override fun init() {
        (context as MainActivity).setStateBn(true, "user")

        val tabTextList = listOf(getString(R.string.noti_activity_tab), getString(R.string.noti_exhibition_tab))
// Kotlin 파일의 클래스 내에서 아래 코드를 사용하세요.
        MobileAds.initialize(requireActivity())
        val adLoader = AdLoader.Builder(requireActivity(), "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { nativeAd ->
                binding.natSmallTemplate.setNativeAd(nativeAd)

            }
            .build()

        adLoader.loadAd(AdRequest.Builder().build())



        if (action=="your_special_exhibition"){
            binding.tlNoti.getTabAt(1)?.select()
            binding.vpNoti.adapter = NotiVpAdapter(this.requireActivity())
            TabLayoutMediator(binding.tlNoti, binding.vpNoti){ tab, pos ->
                tab.text = tabTextList[pos]
            }.attach()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.vpNoti.currentItem = 1
            }, 100)
        }else{
            binding.vpNoti.adapter = NotiVpAdapter(this.requireActivity())
            TabLayoutMediator(binding.tlNoti, binding.vpNoti){ tab, pos ->
                tab.text = tabTextList[pos]
            }.attach()

        }
    }
}