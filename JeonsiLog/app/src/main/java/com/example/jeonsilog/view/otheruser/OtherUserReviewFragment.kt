package com.example.jeonsilog.view.otheruser

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentOtherUserReviewBinding
import com.example.jeonsilog.viewmodel.OtherUserViewModel
import com.example.jeonsilog.widget.utils.SpannableStringUtil

class OtherUserReviewFragment(private val vm: OtherUserViewModel): BaseFragment<FragmentOtherUserReviewBinding>(R.layout.fragment_other_user_review) {
    override fun init() {
        val list = mutableListOf<OtherUserReviewModel>()
        list.add(
            OtherUserReviewModel(
                1,
                "https://picsum.photos/id/200/200/300",
                "[전시회 이름]\n재미있게 관람했습니다. 특이한 작품도 많았고 보는 내내 즐거웠어요. 주변에도 소개해 줄 생각이에요. 특히 뎁스 작가의 작 주변에도 소개해 줄 생각이에요. 특히 뎁스 작가의 작"
            )
        )
        list.add(
            OtherUserReviewModel(
                2,
                "https://picsum.photos/id/201/200/300",
                "[김은영 : 서로를 안아주는 관계의 존재]\n재미있게 관람했습니다. 특이한 작품도 많았고 보는 내내 즐거웠어요. 주변에도 소개해 줄 생각이에요. 특소개해 줄 생각이에요. 특히 뎁스 작가의 작"
            )
        )
        list.add(
            OtherUserReviewModel(
                1,
                "https://picsum.photos/id/200/200/300",
                "[전시회 이름]\n재미있게 관람했습니다. 특이한 작품도 많았고 보는 내내 즐거웠어요. 주변에도 소개해 줄 생각이에요. 특히 뎁스 작가의 작 주변에도 소개해 줄 생각이에요. 특히 뎁스 작가의 작"
            )
        )
        list.add(
            OtherUserReviewModel(
                2,
                "https://picsum.photos/id/201/200/300",
                "[김은영 : 서로를 안아주는 관계의 존재]\n재미있게 관람했습니다. 특이한 작품도 많았고 보는 내내 즐거웠어요. 주변에도 소개해 줄 생각이에요. 특소개해 줄 생각이에요. 특히 뎁스 작가의 작"
            )
        )
        list.add(
            OtherUserReviewModel(
                1,
                "https://picsum.photos/id/200/200/300",
                "[전시회 이름]\n재미있게 관람했습니다. 특이한 작품도 많았고 보는 내내 즐거웠어요. 주변에도 소개해 줄 생각이에요. 특히 뎁스 작가의 작 주변에도 소개해 줄 생각이에요. 특히 뎁스 작가의 작"
            )
        )
        list.add(
            OtherUserReviewModel(
                2,
                "https://picsum.photos/id/201/200/300",
                "[김은영 : 서로를 안아주는 관계의 존재]\n재미있게 관람했습니다. 특이한 작품도 많았고 보는 내내 즐거웠어요. 주변에도 소개해 줄 생각이에요. 특소개해 줄 생각이에요. 특히 뎁스 작가의 작"
            )
        )

        if (list.isEmpty()) {
            binding.rvOtherUserReview.visibility = View.GONE
            binding.tvOtherUserReviewCount.visibility = View.GONE
            binding.ivOtherUserReviewEmptyImg.visibility = View.VISIBLE
            binding.tvOtherUserReviewEmptyTitle.visibility = View.VISIBLE
        } else {
            val adapter = OtherUserRvAdapter<OtherUserReviewModel>(list, 1)
            binding.rvOtherUserReview.adapter = adapter
            binding.rvOtherUserReview.layoutManager = LinearLayoutManager(requireContext())
            binding.rvOtherUserReview.addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayout.VERTICAL
                )
            )

            binding.tvOtherUserReviewCount.text = SpannableStringUtil().highlightNumber(
                getString(
                    R.string.other_review_count,
                    vm.nick.value,
                    list.size
                ),
                requireContext()
            )
        }
    }
}