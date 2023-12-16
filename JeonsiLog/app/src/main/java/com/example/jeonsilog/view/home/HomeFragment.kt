package com.example.jeonsilog.view.home

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentHomeBinding
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.viewmodel.HomeRvModel
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.extraActivityReference

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private lateinit var homeRvAdapter: HomeRvAdapter
    override fun init() {
        val list = listOf<HomeRvModel>(
            HomeRvModel(0,"title","address","place","",""),
            HomeRvModel(1,"title","address","place","",""),
            HomeRvModel(2,"title","address","place","",""),
            HomeRvModel(3,"title","address","place","",""),
            HomeRvModel(4,"title","address","place","",""),
            HomeRvModel(5,"title","address","place","",""),
            HomeRvModel(6,"title","address","place","",""),
            HomeRvModel(7,"title","address","place","",""),
            HomeRvModel(8,"title","address","place","",""),
            HomeRvModel(9,"title","address","place","",""),
            HomeRvModel(10,"title","address","place","",""),
            HomeRvModel(11,"title","address","place","","")
        )
        homeRvAdapter = HomeRvAdapter(list)
        binding.rvHomeExhibition.adapter = homeRvAdapter
        binding.rvHomeExhibition.layoutManager = LinearLayoutManager(this.context)

        binding.btnGoExhibition.setOnClickListener{
            (activity as MainActivity).loadExtraActivity(0)
        }
    }

}