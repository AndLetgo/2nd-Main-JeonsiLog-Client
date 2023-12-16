package com.example.jeonsilog.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.ExhibitionInfoItem
import com.example.jeonsilog.data.remote.dto.ExhibitionPlaceItem

import com.example.jeonsilog.data.remote.dto.Test_Data
import com.example.jeonsilog.databinding.FragmentExhibitionPlaceBinding
import com.example.jeonsilog.databinding.FragmentExihibitionInfoBinding
import com.example.jeonsilog.viewmodel.SearchViewModel


class ExhibitionPlaceFragment(itemlist: List<Int>) : BaseFragment<FragmentExhibitionPlaceBinding>(R.layout.fragment_exhibition_place) {
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
        binding.rvExhibitionplace.layoutManager = LinearLayoutManager(requireContext())
        var items=extractItemsByIndices(viewModel.exhibitionplacelist,itemlist)
        val adapter = context?.let { ExhibitionPlaceItemAdapter(it,items) }
        binding.rvExhibitionplace.adapter = adapter
    }
    fun extractItemsByIndices(exhibitionList: List<ExhibitionPlaceItem>, indices: List<Int>): List<ExhibitionPlaceItem> {
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