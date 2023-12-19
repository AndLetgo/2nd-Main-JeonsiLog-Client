package com.example.jeonsilog.view.mypage

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentMyPageReviewBinding
import com.example.jeonsilog.widget.utils.SpannableStringUtil

class MyPageReviewFragment: BaseFragment<FragmentMyPageReviewBinding>(R.layout.fragment_my_page_review) {
    override fun init() {
        val list = mutableListOf<MyPageReviewModel>()
        list.add(
            MyPageReviewModel(
                1,
                "https://picsum.photos/id/200/200/300",
                "[전시회 이름]\n재미있게 관람했습니다. 특이한 작품도 많았고 보는 내내 즐거웠어요. 주변에도 소개해 줄 생각이에요. 특히 뎁스 작가의 작 주변에도 소개해 줄 생각이에요. 특히 뎁스 작가의 작"
            )
        )
        list.add(
            MyPageReviewModel(
                2,
                "https://picsum.photos/id/201/200/300",
                "[김은영 : 서로를 안아주는 관계의 존재]\n재미있게 관람했습니다. 특이한 작품도 많았고 보는 내내 즐거웠어요. 주변에도 소개해 줄 생각이에요. 특소개해 줄 생각이에요. 특히 뎁스 작가의 작"
            )
        )

        if (list.isEmpty()) {
            binding.rvMypageReview.visibility = View.GONE
            binding.tvMypageReviewCount.visibility = View.GONE
            binding.ivMypageReviewEmptyImg.visibility = View.VISIBLE
            binding.tvMypageReviewEmptyTitle.visibility = View.VISIBLE
            binding.tvMypageReviewEmptyDescription.visibility = View.VISIBLE
        } else {
            val adapter = MyPageRvAdapter<MyPageReviewModel>(list, 1)
            binding.rvMypageReview.adapter = adapter
            binding.rvMypageReview.layoutManager = LinearLayoutManager(requireContext())
            binding.rvMypageReview.addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayout.VERTICAL
                )
            )

            binding.tvMypageReviewCount.text = SpannableStringUtil().highlightNumber(
                getString(
                    R.string.mypage_review_count,
                    list.size
                ),
                requireContext()
            )
        }
    }
}