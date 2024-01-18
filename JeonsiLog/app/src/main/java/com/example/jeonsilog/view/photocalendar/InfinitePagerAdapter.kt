package com.example.jeonsilog.view.photocalendar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class InfinitePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = Int.MAX_VALUE
    override fun createFragment(position: Int): Fragment {
        // Create and return your fragment instance based on the position
        return SingleFragment(position)
    }

}