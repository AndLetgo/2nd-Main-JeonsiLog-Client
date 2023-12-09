package com.example.jeonsilog.view.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.jeonsilog.data.remote.dto.ExhibitionInfoItem


class SearchResultAdapter(fm: FragmentManager, lifecycle: Lifecycle, itemstr:String) : FragmentStateAdapter(fm, lifecycle) {
    var edittext=itemstr
    val items = listOf(
        ExhibitionInfoItem(
            edittext+"전시회1",
            edittext+"위치1",
            edittext+"장소1",
            edittext+"가격1",
            edittext+"날짜1"
        ),
        ExhibitionInfoItem(
            edittext+"전시회2",
            edittext+"위치2",
            edittext+"장소2",
            edittext+"가격2",
            edittext+"날짜2"
        ),
        // 필요한 만큼 더 많은 아이템을 추가할 수 있습니다.
    )
    override fun getItemCount(): Int = 3 // 탭의 개수

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ExhibitionInfoFragment(items)
            1 -> ExhibitionPlaceFragment()
            2 -> UserSearchFragment()
            // 추가적인 탭에 대한 처리도 추가 가능
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}