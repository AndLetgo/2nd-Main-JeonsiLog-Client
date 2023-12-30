package com.example.jeonsilog.view.mypage

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentMyPageListFollowerBinding

class MyPageListFollowerFragment: BaseFragment<FragmentMyPageListFollowerBinding>(R.layout.fragment_my_page_list_follower) {

    override fun init() {
        val list = mutableListOf<MyPageListFollowerModel>()
        list.add(MyPageListFollowerModel(1, "https://picsum.photos/id/200/200/300", "카라멜", false))
        list.add(MyPageListFollowerModel(2, "https://picsum.photos/id/201/200/300", "문리나", false))
        list.add(MyPageListFollowerModel(3, "https://picsum.photos/id/202/200/300", "메이첼", false))
        list.add(MyPageListFollowerModel(4, "https://picsum.photos/id/203/200/300", "안드레고", true))
        list.add(MyPageListFollowerModel(5, "https://picsum.photos/id/204/200/300", "치즈감자전", true))
        list.add(MyPageListFollowerModel(6, "https://picsum.photos/id/205/200/300", "제주감귤한라봉", true))
        list.add(MyPageListFollowerModel(7, "https://picsum.photos/id/206/200/300", "감기조심하세요", true))
        list.add(MyPageListFollowerModel(8, "https://picsum.photos/id/207/200/300", "마라엽떡", true))


        if(list.isEmpty()){
            binding.rvMypageFollower.visibility = View.GONE
            binding.ivMypageListFollowerEmptyImg.visibility = View.VISIBLE
            binding.tvMypageListFollowerEmptyTitle.visibility = View.VISIBLE
        } else {
            val adapter = MyPageListRvAdapter<MyPageListFollowerModel>(list, 0, requireContext())
            binding.rvMypageFollower.adapter = adapter
            binding.rvMypageFollower.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}