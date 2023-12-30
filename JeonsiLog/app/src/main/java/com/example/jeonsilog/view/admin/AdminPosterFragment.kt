package com.example.jeonsilog.view.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentAdminPosterBinding
import com.example.jeonsilog.view.exhibition.AdminPosterVpAdapter
import com.example.jeonsilog.viewmodel.ExhibitionPosterViewModel

class AdminPosterFragment : BaseFragment<FragmentAdminPosterBinding>(R.layout.fragment_admin_poster) {
    private lateinit var posterList:List<Int>
    private val viewModel:ExhibitionPosterViewModel by viewModels()
    private lateinit var viewPager: ViewPager
    override fun init() {
        viewPager = binding.vpPoster

        posterList = listOf<Int>(
            R.drawable.illus_dialog_delete,
            R.drawable.illus_dialog_report,
            R.drawable.illus_empty_poster)
        viewModel.setMaxCount(posterList.size.toString())
//        val adapter = this.context?.let { PosterVpAdapter(posterList, it) }
        val adapter = AdminPosterVpAdapter(posterList, requireContext())
        adapter.setCountListener(object : AdminPosterVpAdapter.CountListener{
            override fun setCount(position: Int) {
                viewModel.setCount((position+1).toString())
            }
        })
        adapter.setOnPageChangeListener(viewPager)
        binding.vpPoster.adapter = adapter

        binding.vm = viewModel
        viewModel.count.observe(this){
            binding.tvCountPoster.text = it
        }

        binding.ibBack.setOnClickListener{
            val currentIndex = viewPager.currentItem
            if(currentIndex > 0 ){
                viewPager.setCurrentItem(currentIndex-1,true)
            }
        }
        binding.ibNext.setOnClickListener{
            val currentIndex = viewPager.currentItem
            if(currentIndex < posterList.size){
                viewPager.setCurrentItem(currentIndex+1,true)
            }
        }
    }
}