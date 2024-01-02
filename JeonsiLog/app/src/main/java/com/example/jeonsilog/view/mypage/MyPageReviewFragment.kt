package com.example.jeonsilog.view.mypage

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.review.GetReviewsDataEntity
import com.example.jeonsilog.databinding.FragmentMyPageReviewBinding
import com.example.jeonsilog.repository.review.ReviewRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.SpannableStringUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MyPageReviewFragment: BaseFragment<FragmentMyPageReviewBinding>(R.layout.fragment_my_page_review) {
    private var numReview = 0
    private var list = mutableListOf<GetReviewsDataEntity>()
    override fun init() {
        runBlocking(Dispatchers.IO){
            val response = ReviewRepositoryImpl().getMyReviews(GlobalApplication.encryptedPrefs.getAT())
            if(response.isSuccessful && response.body()!!.check){
                numReview = response.body()!!.information.numReview
                val data = response.body()!!.information.dataEntity.listIterator()
                while (data.hasNext()){
                    val temp = data.next()
                    list.add(
                        GetReviewsDataEntity(
                            reviewId = temp.reviewId,
                            exhibitionId = temp.exhibitionId,
                            exhibitionName = "[${temp.exhibitionName}]",
                            contents = temp.contents,
                            exhibitionImgUrl = temp.exhibitionImgUrl
                        )
                    )
                }
            }
        }

        if (list.isEmpty()) {
            binding.rvMypageReview.visibility = View.GONE
            binding.tvMypageReviewCount.visibility = View.GONE
            binding.ivMypageReviewEmptyImg.visibility = View.VISIBLE
            binding.tvMypageReviewEmptyTitle.visibility = View.VISIBLE
            binding.tvMypageReviewEmptyDescription.visibility = View.VISIBLE
        } else {
            val adapter = MyPageRvAdapter<GetReviewsDataEntity>(list, 1)
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
                    numReview
                ),
                requireContext()
            )
        }
    }
}