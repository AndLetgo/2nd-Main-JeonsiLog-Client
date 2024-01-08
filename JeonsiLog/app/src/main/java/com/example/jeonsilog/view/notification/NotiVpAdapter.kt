package com.example.jeonsilog.view.notification

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class NotiVpAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> NotiActivityFragment()
            1 -> NotiExhibitionFragment()
            else -> throw IllegalArgumentException("알 수 없는 프래그먼트 타입")
        }
    }
}