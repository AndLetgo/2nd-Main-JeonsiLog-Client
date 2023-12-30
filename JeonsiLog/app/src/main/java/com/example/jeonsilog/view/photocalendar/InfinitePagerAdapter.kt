package com.example.jeonsilog.view.photocalendar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.jeonsilog.viewmodel.PhotoCalendarViewModel


class InfinitePagerAdapter(fragmentActivity: FragmentActivity, val viewModel: PhotoCalendarViewModel) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = Int.MAX_VALUE
    override fun createFragment(position: Int): Fragment {
        // Create and return your fragment instance based on the position
        return SingleFragment(position,viewModel)
    }

}