package com.example.jeonsilog.view.mypage

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.review.GetReviewsEntity
import com.example.jeonsilog.databinding.FragmentMyPageReviewBinding
import com.example.jeonsilog.repository.review.ReviewRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.SpannableStringUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MyPageReviewFragment: BaseFragment<FragmentMyPageReviewBinding>(R.layout.fragment_my_page_review) {
    private lateinit var adapter: MyPageRvAdapter<GetReviewsEntity>
    private var numReview = 0
    private var list = mutableListOf<GetReviewsEntity>()
    private var page = 0
    private var isFinished = false
    private var newItemCount = 0

    override fun init() {
        getItems()

        if (list.isEmpty()) {
            binding.rvMypageReview.visibility = View.GONE
            binding.tvMypageReviewCount.visibility = View.GONE
            binding.ivMypageReviewEmptyImg.visibility = View.VISIBLE
            binding.tvMypageReviewEmptyTitle.visibility = View.VISIBLE
            binding.tvMypageReviewEmptyDescription.visibility = View.VISIBLE
        } else {
            adapter = MyPageRvAdapter<GetReviewsEntity>(list, 1, requireContext())
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

            binding.rvMypageReview.addOnScrollListener(object: RecyclerView.OnScrollListener(){
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
            val response = ReviewRepositoryImpl().getMyReviews(GlobalApplication.encryptedPrefs.getAT(), page)
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
                            exhibitionImgUrl = temp.exhibitionImgUrl
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