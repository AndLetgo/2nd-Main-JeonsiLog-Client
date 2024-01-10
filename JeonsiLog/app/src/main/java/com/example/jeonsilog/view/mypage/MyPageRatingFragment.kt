package com.example.jeonsilog.view.mypage

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.rating.GetMyRatingsDataEntity
import com.example.jeonsilog.databinding.FragmentMyPageRatingBinding
import com.example.jeonsilog.repository.rating.RatingRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.SpannableStringUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MyPageRatingFragment:BaseFragment<FragmentMyPageRatingBinding>(R.layout.fragment_my_page_rating) {
    private var numRating = 0
    private var list = mutableListOf<GetMyRatingsDataEntity>()
    private var page = 0
    private var isFinished = false
    private var newItemCount = 0
    private lateinit var adapter: MyPageRvAdapter<GetMyRatingsDataEntity>

    override fun init() {
        getItems()

        if(list.isEmpty()){
            binding.rvMypageRating.visibility = View.GONE
            binding.tvMypageRatingCount.visibility = View.GONE
            binding.ivMypageRatingEmptyImg.visibility = View.VISIBLE
            binding.tvMypageRatingEmptyTitle.visibility = View.VISIBLE
            binding.tvMypageRatingEmptyDescription.visibility =View.VISIBLE
        } else {

            adapter = MyPageRvAdapter<GetMyRatingsDataEntity>(list, 0, requireContext())
            binding.rvMypageRating.adapter = adapter
            binding.rvMypageRating.layoutManager = LinearLayoutManager(requireContext())
            binding.rvMypageRating.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            binding.tvMypageRatingCount.text = SpannableStringUtil().highlightNumber(getString(R.string.mypage_my_rating_count, numRating), requireContext())

            binding.rvMypageRating.addOnScrollListener(object: RecyclerView.OnScrollListener(){
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

    private fun getItems() {
        runBlocking(Dispatchers.IO) {
            val response = RatingRepositoryImpl().getMyRatings(encryptedPrefs.getAT(), page)
            if (response.isSuccessful && response.body()!!.check) {
                newItemCount = response.body()!!.information.dataEntity.size
                numRating = response.body()!!.information.numRating
                val data = response.body()!!.information.dataEntity.listIterator()
                while (data.hasNext()) {
                    val temp = data.next()
                    list.add(
                        GetMyRatingsDataEntity(
                            ratingId = temp.ratingId,
                            exhibitionId = temp.exhibitionId,
                            exhibitionName = "[${temp.exhibitionName}]",
                            rate = temp.rate
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