package com.example.jeonsilog.view.admin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdminSearchVpAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> AdminSearchExhibitionFragment()
            1 -> AdminSearchPlaceFragment()
            else -> AdminSearchUserFragment()
        }
    }
}