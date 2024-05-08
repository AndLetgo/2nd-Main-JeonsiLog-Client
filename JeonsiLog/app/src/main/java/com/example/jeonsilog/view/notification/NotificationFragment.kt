package com.example.jeonsilog.view.notification

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentNotificationBinding
import com.example.jeonsilog.view.MainActivity
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayoutMediator

class NotificationFragment(private val action:String) : BaseFragment<FragmentNotificationBinding>(
    R.layout.fragment_notification) {

    override fun init() {
        (context as MainActivity).setStateBn(true, "user")

        val tabTextList = listOf(getString(R.string.noti_activity_tab), getString(R.string.noti_exhibition_tab))
        val background = ColorDrawable(Color.TRANSPARENT)

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        //네이티브 테스트용 광고 ID : ca-app-pub-3940256099942544/4177191038
//        MobileAds.initialize(requireActivity())
//        val adLoader = AdLoader.Builder(requireActivity(), "ca-app-pub-3940256099942544/2247696110")
//            .forNativeAd { nativeAd ->
//                // Create NativeTemplateStyle
//                val styles = NativeTemplateStyle.Builder().withMainBackgroundColor(background).build()
//
//                binding.natSmallTemplate.setStyles(styles)
//                binding.natSmallTemplate.setNativeAd(nativeAd)
//                Log.d("AdLoad", "광고가 성공적으로 로드되었습니다.")
//            }
//            .withAdListener(object : AdListener() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
//                    super.onAdFailedToLoad(adError)
//                    Log.d("AdLoad", "광고 로드 실패: ${adError.message}")
//                }
//            })
//            .build()
//
//        adLoader.loadAds(AdRequest.Builder().build(), 3)



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