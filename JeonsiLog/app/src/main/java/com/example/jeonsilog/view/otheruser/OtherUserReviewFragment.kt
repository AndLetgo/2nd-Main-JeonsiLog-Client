package com.example.jeonsilog.view.otheruser

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.review.GetReviewsDataEntity
import com.example.jeonsilog.databinding.FragmentOtherUserReviewBinding
import com.example.jeonsilog.repository.review.ReviewRepositoryImpl
import com.example.jeonsilog.viewmodel.OtherUserViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.SpannableStringUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class OtherUserReviewFragment(private val vm: OtherUserViewModel, private val otherUserId: Int): BaseFragment<FragmentOtherUserReviewBinding>(R.layout.fragment_other_user_review) {
    private var numReview = 0
    private val list = mutableListOf<GetReviewsDataEntity>()

    override fun init() {
        runBlocking(Dispatchers.IO){
            val response = ReviewRepositoryImpl().getOtherReviews(GlobalApplication.encryptedPrefs.getAT(), otherUserId)
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
            binding.rvOtherUserReview.visibility = View.GONE
            binding.tvOtherUserReviewCount.visibility = View.GONE
            binding.ivOtherUserReviewEmptyImg.visibility = View.VISIBLE
            binding.tvOtherUserReviewEmptyTitle.visibility = View.VISIBLE
        } else {
            val adapter = OtherUserRvAdapter<GetReviewsDataEntity>(list, 1)
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

            vm.nick.observe(this){
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
}