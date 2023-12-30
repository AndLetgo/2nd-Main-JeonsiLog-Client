package com.example.jeonsilog.view.search

import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.ExhibitionInfoItem
import com.example.jeonsilog.viewmodel.SearchViewModel
import com.example.jeonsilog.databinding.FragmentExihibitionInfoBinding

class ExhibitionInfoFragment(itemlist: List<Int>): BaseFragment<FragmentExihibitionInfoBinding>(R.layout.fragment_exihibition_info){
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
        binding.rvExhibitioninfo.layoutManager = LinearLayoutManager(requireContext())
        var items=extractItemsByIndices(viewModel.exhibitionlist,itemlist)
        val adapter = context?.let { ExhibitionInfoItemAdapter(it,items) }
        binding.rvExhibitioninfo.adapter = adapter
    }
    fun extractItemsByIndices(exhibitionList: List<ExhibitionInfoItem>, indices: List<Int>): List<ExhibitionInfoItem> {
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