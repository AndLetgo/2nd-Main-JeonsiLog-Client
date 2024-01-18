package com.example.jeonsilog.view.search

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class SearchResultAdapter(fm: FragmentManager, lifecycle: Lifecycle, itemstr:String) : FragmentStateAdapter(fm, lifecycle) {
    var edittext=itemstr

    override fun getItemCount(): Int = 3 // 탭의 개수

    override fun createFragment(position: Int): Fragment {
        Log.d("SearchResultAdapter", "Creating Fragment for Position: $position")

        return when (position) {
            0 -> ExhibitionInfoFragment(edittext)

            1 -> ExhibitionPlaceFragment(edittext)

            2 -> UserSearchFragment(edittext)
            // 추가적인 탭에 대한 처리도 추가 가능
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}