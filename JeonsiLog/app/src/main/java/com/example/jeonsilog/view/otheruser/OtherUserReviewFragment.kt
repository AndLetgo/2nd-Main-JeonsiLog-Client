package com.example.jeonsilog.view.otheruser

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.review.GetReviewsEntity
import com.example.jeonsilog.databinding.FragmentOtherUserReviewBinding
import com.example.jeonsilog.repository.review.ReviewRepositoryImpl
import com.example.jeonsilog.viewmodel.OtherUserViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.SpannableStringUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class OtherUserReviewFragment(private val vm: OtherUserViewModel, private val otherUserId: Int): BaseFragment<FragmentOtherUserReviewBinding>(R.layout.fragment_other_user_review) {
    private var numReview = 0
    private val list = mutableListOf<GetReviewsEntity>()
    private var page = 0
    private var isFinished = false
    private var newItemCount = 0
    private lateinit var adapter: OtherUserRvAdapter<GetReviewsEntity>

    override fun init() {
        getItems()

        if (list.isEmpty()) {
            binding.rvOtherUserReview.visibility = View.GONE
            binding.tvOtherUserReviewCount.visibility = View.GONE
            binding.ivOtherUserReviewEmptyImg.visibility = View.VISIBLE
            binding.tvOtherUserReviewEmptyTitle.visibility = View.VISIBLE
        } else {
            adapter = OtherUserRvAdapter<GetReviewsEntity>(list, 1, requireContext())
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

            binding.rvOtherUserReview.addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    val totalCount = recyclerView.adapter?.itemCount?.minus(1)

                    if(totalCount == rvPosition){
                        if(!isFinished){
                            getItems()

                            recyclerView.post {
                                adapter.notifyItemRangeInserted(totalCount+1, newItemCount)
                                newItemCount = 0
                            }
                        }
                    }
                }
            })
        }
    }

    private fun getItems(){
        runBlocking(Dispatchers.IO){
            val response = ReviewRepositoryImpl().getOtherReviews(GlobalApplication.encryptedPrefs.getAT(), otherUserId, page)
            if(response.isSuccessful && response.body()!!.check){
                newItemCount = response.body()!!.information.data.size
                numReview = response.body()!!.information.numReview
                val data = response.body()!!.information.data.listIterator()
                while (data.hasNext()){
                    val temp = data.next()
                    list.add(
                        GetReviewsEntity(
                            reviewId = temp.reviewId,
                            exhibitionId = temp.exhibitionId,
                            exhibitionName = "[${temp.exhibitionName}]",
                            contents = temp.contents,
                            exhibitionImgUrl = temp.exhibitionImgUrl,
                            createdDate = temp.createdDate
                        )
                    )
                }
            } else {
                isFinished = true
            }

            page++
        }

    }
}