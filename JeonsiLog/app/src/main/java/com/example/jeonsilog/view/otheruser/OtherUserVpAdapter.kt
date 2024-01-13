package com.example.jeonsilog.view.otheruser

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.jeonsilog.viewmodel.OtherUserViewModel

class OtherUserVpAdapter(fragmentActivity: FragmentActivity, private val vm: OtherUserViewModel, private val otherUserId: Int): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> OtherUserRatingFragment(vm, otherUserId)
            1 -> OtherUserReviewFragment(vm, otherUserId)
            2 -> OtherUserCalendarFragment(otherUserId)
            else -> throw IllegalArgumentException("유효하지 않은 인덱스 번호: $position")
        }
    }
}