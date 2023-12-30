package com.example.jeonsilog.view.mypage

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentMyPageListFollowingBinding

class MyPageListFollowingFragment: BaseFragment<FragmentMyPageListFollowingBinding>(R.layout.fragment_my_page_list_following) {
    override fun init() {
        val list = mutableListOf<MyPageListFollowingModel>()
        list.add(MyPageListFollowingModel(1, "https://picsum.photos/id/200/200/300", "카라멜"))
        list.add(MyPageListFollowingModel(2, "https://picsum.photos/id/201/200/300", "문리나"))
        list.add(MyPageListFollowingModel(3, "https://picsum.photos/id/202/200/300", "메이첼"))
        list.add(MyPageListFollowingModel(4, "https://picsum.photos/id/203/200/300", "안드레고"))
        list.add(MyPageListFollowingModel(5, "https://picsum.photos/id/204/200/300", "치즈감자전"))
        list.add(MyPageListFollowingModel(6, "https://picsum.photos/id/205/200/300", "제주감귤한라봉"))
        list.add(MyPageListFollowingModel(7, "https://picsum.photos/id/206/200/300", "감기조심하세요"))
        list.add(MyPageListFollowingModel(8, "https://picsum.photos/id/207/200/300", "마라엽떡"))


        if(list.isEmpty()){
            binding.rvMypageFollowing.visibility = View.GONE
            binding.ivMypageListFollowingEmptyImg.visibility = View.VISIBLE
            binding.tvMypageListFollowingEmptyTitle.visibility = View.VISIBLE
            binding.tvMypageListFollowingEmptyDescription.visibility = View.VISIBLE
        } else {
            val adapter = MyPageListRvAdapter<MyPageListFollowingModel>(list, 1, requireContext())
            binding.rvMypageFollowing.adapter = adapter
            binding.rvMypageFollowing.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}