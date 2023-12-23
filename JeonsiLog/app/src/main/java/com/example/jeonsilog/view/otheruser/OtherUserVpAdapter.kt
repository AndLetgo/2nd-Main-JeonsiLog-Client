package com.example.jeonsilog.view.otheruser

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OtherUserVpAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> OtherUserRatingFragment()
            1 -> OtherUserReviewFragment()
            2 -> OtherUserCalendarFragment()
            else -> throw IllegalArgumentException("유효하지 않은 인덱스 번호: $position")
        }
    }
}