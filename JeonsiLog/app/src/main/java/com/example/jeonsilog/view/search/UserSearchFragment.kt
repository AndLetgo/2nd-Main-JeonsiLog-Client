package com.example.jeonsilog.view.search

import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.UserSearchItem
import com.example.jeonsilog.databinding.FragmentUserSearchBinding
import com.example.jeonsilog.viewmodel.SearchViewModel


class UserSearchFragment(itemlist: List<Int>) : BaseFragment<FragmentUserSearchBinding>(R.layout.fragment_user_search) {
    var itemlist=itemlist
    lateinit var viewModel: SearchViewModel
    override fun init() {
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        checkEmptyList()
        setLayoutView()
    }
    fun checkEmptyList(){
        if(itemlist.size!=0){
            binding.ivEmpty.isGone=true
            binding.tvEmpty01.isGone=true
            binding.tvEmpty02.isGone=true
        }
    }
    fun setLayoutView(){
        //리사이클러뷰 제어
        binding.rvUserinfo.layoutManager = LinearLayoutManager(requireContext())
        var items=extractItemsByIndices(viewModel.userlist,itemlist)
        val adapter = context?.let { UserSearchItemAdapter(it,items) }
        binding.rvUserinfo.adapter = adapter
    }
    fun extractItemsByIndices(exhibitionList: List<UserSearchItem>, indices: List<Int>): List<UserSearchItem> {
        //검색해서 나온 인덱스 리스트를 전시회 리스트에서 추출
        return indices.mapNotNull { index ->
            if (index in exhibitionList.indices) {
                exhibitionList[index]
            } else {
                null
            }
        }
    }
}