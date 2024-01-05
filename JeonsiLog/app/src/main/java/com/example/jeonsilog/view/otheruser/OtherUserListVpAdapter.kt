package com.example.jeonsilog.view.otheruser

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OtherUserListVpAdapter(fragmentActivity: FragmentActivity, private val otherUserId: Int): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> OtherUserListFollowerFragment(otherUserId)
            1 -> OtherUserListFollowingFragment(otherUserId)
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}