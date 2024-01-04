package com.example.jeonsilog.view.mypage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPageVpAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MyPageRatingFragment()
            1 -> MyPageReviewFragment()
            else -> MyPageInterestFragment()
        }
    }
}