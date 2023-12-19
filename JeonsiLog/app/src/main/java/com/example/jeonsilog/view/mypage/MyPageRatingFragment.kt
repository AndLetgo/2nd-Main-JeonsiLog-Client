package com.example.jeonsilog.view.mypage

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentMyPageRatingBinding
import com.example.jeonsilog.widget.utils.SpannableStringUtil

class MyPageRatingFragment:BaseFragment<FragmentMyPageRatingBinding>(R.layout.fragment_my_page_rating) {
    override fun init() {
        val list = mutableListOf<MyPageRatingModel>()
        list.add(MyPageRatingModel(1, "[CONNECT]", 2.5f))
        list.add(MyPageRatingModel(2, "[안드레고]", 4f))
        list.add(MyPageRatingModel(3, "[전시로그]", 4.5f))
        list.add(MyPageRatingModel(4, "[DEPth]", 5f))

        if(list.isEmpty()){
            binding.rvMypageRating.visibility = View.GONE
            binding.tvMypageRatingCount.visibility = View.GONE
            binding.ivMypageRatingEmptyImg.visibility = View.VISIBLE
            binding.tvMypageRatingEmptyTitle.visibility = View.VISIBLE
            binding.tvMypageRatingEmptyDescription.visibility =View.VISIBLE
        } else {
            val adapter = MyPageRvAdapter(list, 0)
            binding.rvMypageRating.adapter = adapter
            binding.rvMypageRating.layoutManager = LinearLayoutManager(requireContext())
            binding.rvMypageRating.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))

            binding.tvMypageRatingCount.text = SpannableStringUtil().highlightNumber(getString(R.string.mypage_my_rating_count, list.size), requireContext())
        }
    }
}