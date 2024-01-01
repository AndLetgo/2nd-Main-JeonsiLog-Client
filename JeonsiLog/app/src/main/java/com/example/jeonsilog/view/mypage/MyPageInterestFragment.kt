package com.example.jeonsilog.view.mypage

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.interest.GetInterestInformationEntity
import com.example.jeonsilog.databinding.FragmentMyPageInterestBinding

class MyPageInterestFragment:BaseFragment<FragmentMyPageInterestBinding>(R.layout.fragment_my_page_interest) {
    override fun init() {
        val list = mutableListOf<GetInterestInformationEntity>()

        if(list.isEmpty()){
            binding.rvMypageInterest.visibility = View.GONE
            binding.ivMypageInterestEmptyImg.visibility = View.VISIBLE
            binding.tvMypageInterestEmptyTitle.visibility = View.VISIBLE
            binding.tvMypageReviewEmptyDescription.visibility = View.VISIBLE
        } else {
            val adapter = MyPageRvAdapter<GetInterestInformationEntity>(list, 2)
            binding.rvMypageInterest.adapter = adapter
            binding.rvMypageInterest.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}