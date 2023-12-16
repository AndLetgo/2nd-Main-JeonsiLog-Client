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

    val resultIndexExhibitionInfo = findItemIndicesByExhibitionName(viewModel.exhibitionlist, edittext)
    val resultIndexExhibitionPlace = findItemIndicesByExhibitionPlace(viewModel.exhibitionplacelist, edittext)
    val resultIndexUser = findItemIndicesByUser(viewModel.userlist, edittext)
    init {
        // 초기 탭을 설정합니다.
        setCurrentTabPosition(initialTabPosition)
    }
    override fun getItemCount(): Int = 3 // 탭의 개수

    override fun createFragment(position: Int): Fragment {
        Log.d("SearchResultAdapter", "Creating Fragment for Position: $position")

        return when (position) {
            0 -> ExhibitionInfoFragment(resultIndexExhibitionInfo)

            1 -> ExhibitionPlaceFragment(resultIndexExhibitionPlace)

            2 -> UserSearchFragment(resultIndexUser)
            // 추가적인 탭에 대한 처리도 추가 가능
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
    fun findItemIndicesByExhibitionName(exhibitionList: List<ExhibitionInfoItem>, search: String): List<Int> {
        val indices = mutableListOf<Int>()
        for ((index, item) in exhibitionList.withIndex()) {
            if (item.exhibitionName?.contains(search, ignoreCase = true) == true ||
                item.exhibitionLocation?.contains(search, ignoreCase = true) == true ||
                item.exhibitionPlace?.contains(search, ignoreCase = true) == true ||
                item.exhibitionPrice?.contains(search, ignoreCase = true) == true ||
                item.exhibitionDate?.contains(search, ignoreCase = true) == true
            ) {
                indices.add(index)
            }
        }
        return indices
    }
    fun findItemIndicesByExhibitionPlace(exhibitionList: List<ExhibitionPlaceItem>, search: String): List<Int> {
        val indices = mutableListOf<Int>()
        for ((index, item) in exhibitionList.withIndex()) {
            if (item.exhibitionPlaceItemName?.contains(search, ignoreCase = true) == true ||
                item.exhibitionPlaceLocation?.contains(search, ignoreCase = true) == true
            ) {
                indices.add(index)
            }
        }
        return indices
    }

    fun findItemIndicesByUser(exhibitionList: List<UserSearchItem>, search: String): List<Int> {
        val indices = mutableListOf<Int>()
        for ((index, item) in exhibitionList.withIndex()) {
            if (item.username?.contains(search, ignoreCase = true) == true
            ) {
                indices.add(index)
            }
        }
        return indices
    }

    fun setCurrentTabPosition(position: Int) {

        notifyDataSetChanged()
    }




}