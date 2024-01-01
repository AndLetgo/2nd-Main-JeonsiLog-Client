package com.example.jeonsilog.view.mypage

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentMyPageInterestBinding

class MyPageInterestFragment:BaseFragment<FragmentMyPageInterestBinding>(R.layout.fragment_my_page_interest) {
    override fun init() {
        val list = mutableListOf<MyPageInterestModel>()
        list.add(MyPageInterestModel(1, "https://picsum.photos/id/1/200/300", "유혜정 : 평정심", "갤러리 한옥", listOf(KeyWord.on)))
        list.add(MyPageInterestModel(2, "https://picsum.photos/id/2/200/300", "김춘재 : 현현炫玄의 빛", "갤러리 끼 SEOUL", listOf(KeyWord.on, KeyWord.free)))
        list.add(MyPageInterestModel(3, "https://picsum.photos/id/3/200/300", "손다현 개인전", "갤러리 끼 SEOUL", listOf(KeyWord.on, KeyWord.free)))
        list.add(MyPageInterestModel(4, "https://picsum.photos/id/4/200/300", "김춘재 : 현현炫玄의 빛", "갤러리아람", listOf(KeyWord.on)))
        list.add(MyPageInterestModel(5, "https://picsum.photos/id/5/200/300", "우리는 흔들리는 땅에 집을 짓는다?", "챔버1965", listOf(KeyWord.on, KeyWord.free)))
        list.add(MyPageInterestModel(6, "https://picsum.photos/id/6/200/300", "Group by 6", "갤러리 끼 SEOUL", listOf(KeyWord.on, KeyWord.free)))
        list.add(MyPageInterestModel(7, "https://picsum.photos/id/7/200/300", "김은형:서로를 안아주는 관계의 존재", "갤러리 끼 SEOUL", listOf(KeyWord.on, KeyWord.free)))
        list.add(MyPageInterestModel(8, "https://picsum.photos/id/8/200/300", "올:ToGather", "갤러리 끼 SEOUL", listOf(KeyWord.on)))
        list.add(MyPageInterestModel(9, "https://picsum.photos/id/9/200/300", "CONNECT", "갤러리 끼 SEOUL", listOf(KeyWord.before, KeyWord.free)))
        list.add(MyPageInterestModel(10, "https://picsum.photos/id/10/200/300", "이한빈:Islet", "갤러리 끼 SEOUL", listOf(KeyWord.on)))
        list.add(MyPageInterestModel(11, "https://picsum.photos/id/11/200/300", "이야기 발생 시점", "갤러리 끼 SEOUL", listOf(KeyWord.on, KeyWord.free)))

        if(list.isEmpty()){
            binding.rvMypageInterest.visibility = View.GONE
            binding.ivMypageInterestEmptyImg.visibility = View.VISIBLE
            binding.tvMypageInterestEmptyTitle.visibility = View.VISIBLE
            binding.tvMypageReviewEmptyDescription.visibility = View.VISIBLE
        } else {
            val adapter = MyPageRvAdapter<MyPageInterestModel>(list, 2)
            binding.rvMypageInterest.adapter = adapter
            binding.rvMypageInterest.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}