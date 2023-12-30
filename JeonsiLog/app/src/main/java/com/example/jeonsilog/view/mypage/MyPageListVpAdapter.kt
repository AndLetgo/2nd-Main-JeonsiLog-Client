package com.example.jeonsilog.view.mypage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPageListVpAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MyPageListFollowerFragment()
            1 -> MyPageListFollowingFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}