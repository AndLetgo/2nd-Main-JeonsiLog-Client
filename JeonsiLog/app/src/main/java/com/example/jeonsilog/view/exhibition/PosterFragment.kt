package com.example.jeonsilog.view.exhibition

import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentPosterBinding
import com.example.jeonsilog.viewmodel.ExhibitionPosterViewModel

class PosterFragment : BaseFragment<FragmentPosterBinding>(
    R.layout.fragment_poster) {
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
        val adapter = PosterVpAdapter(posterList, requireContext())
        adapter?.setCountListener(object : PosterVpAdapter.CountListener{
            override fun setCount(position: Int) {
                viewModel.setCount((position+1).toString())
            }
        })
        adapter?.setOnPageChangeListener(viewPager)
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

        //이미지 다운로드
        binding.ibDownload.setOnClickListener {
            downloadImageToBitmap()

        }
    }

    //이미지 다운로드
    private fun downloadImageToBitmap(){

    }
    private fun checkStoragePermission() {

    }
    private fun downloadImage(imageUrl:String) {
    }

    //접근 권한
    private fun requestPermission(){

    }

}