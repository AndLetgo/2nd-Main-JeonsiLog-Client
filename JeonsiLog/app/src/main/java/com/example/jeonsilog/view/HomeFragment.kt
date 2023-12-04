package com.example.jeonsilog.view

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentHomeBinding
import com.example.jeonsilog.viewmodel.HomeRvModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private lateinit var homeRvAdapter: HomeRvAdapter
    override fun init() {
        val list = listOf<HomeRvModel>(
            HomeRvModel("title","address","place","",""),
            HomeRvModel("title","address","place","",""),
            HomeRvModel("title","address","place","",""),
            HomeRvModel("title","address","place","",""),
            HomeRvModel("title","address","place","",""),
            HomeRvModel("title","address","place","",""),
            HomeRvModel("title","address","place","",""),
            HomeRvModel("title","address","place","",""),
            HomeRvModel("title","address","place","",""),
            HomeRvModel("title","address","place","",""),
            HomeRvModel("title","address","place","",""),
            HomeRvModel("title","address","place","","")
        )
        homeRvAdapter = HomeRvAdapter(list)
        binding.rvHomeExhibition.adapter = homeRvAdapter
        binding.rvHomeExhibition.layoutManager = LinearLayoutManager(this.context)
    }

}