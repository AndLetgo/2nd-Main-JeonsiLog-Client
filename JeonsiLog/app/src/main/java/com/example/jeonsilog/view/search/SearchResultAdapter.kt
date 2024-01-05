package com.example.jeonsilog.view.search

import android.util.Log
import android.widget.Toast
import androidx.browser.trusted.TokenStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.jeonsilog.data.remote.dto.ExhibitionInfoItem
import com.example.jeonsilog.data.remote.dto.ExhibitionPlaceItem
import com.example.jeonsilog.data.remote.dto.UserSearchItem
import com.example.jeonsilog.viewmodel.SearchViewModel


class SearchResultAdapter(fm: FragmentManager, lifecycle: Lifecycle, itemstr:String,initialTabPosition: Int,private var viewModel: SearchViewModel) : FragmentStateAdapter(fm, lifecycle) {
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