package com.example.jeonsilog.view.otheruser

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.rating.GetMyRatingsDataEntity
import com.example.jeonsilog.databinding.FragmentOtherUserRatingBinding
import com.example.jeonsilog.repository.rating.RatingRepositoryImpl
import com.example.jeonsilog.viewmodel.OtherUserViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.SpannableStringUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class OtherUserRatingFragment(private val vm: OtherUserViewModel, private val otherUserId: Int): BaseFragment<FragmentOtherUserRatingBinding>(R.layout.fragment_other_user_rating) {
    private var numRating = 0
    private var list = mutableListOf<GetMyRatingsDataEntity>()
    private var page = 0
    private var isFinished = false
    private var newItemCount = 0
    private lateinit var adapter: OtherUserRvAdapter<GetMyRatingsDataEntity>

    override fun init() {
        getItems()

        if(list.isEmpty()){
            binding.rvOtherUserRating.visibility = View.GONE
            binding.tvOtherUserRatingCount.visibility = View.GONE
            binding.ivOtherUserRatingEmptyImg.visibility = View.VISIBLE
            binding.tvOtherUserRatingEmptyTitle.visibility = View.VISIBLE
        } else {
            adapter = OtherUserRvAdapter<GetMyRatingsDataEntity>(list, 0, requireContext())
            binding.rvOtherUserRating.adapter = adapter
            binding.rvOtherUserRating.layoutManager = LinearLayoutManager(requireContext())
            binding.rvOtherUserRating.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))

            binding.tvOtherUserRatingCount.text = SpannableStringUtil().highlightNumber(getString(R.string.other_my_rating_count, vm.nick.value ,list.size), requireContext())

            vm.nick.observe(this){
                binding.tvOtherUserRatingCount.text = SpannableStringUtil().highlightNumber(getString(R.string.other_my_rating_count, vm.nick.value ,list.size), requireContext())
            }

            binding.rvOtherUserRating.addOnScrollListener(object: RecyclerView.OnScrollListener(){
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
            val response = RatingRepositoryImpl().getOtherRatings(encryptedPrefs.getAT(), otherUserId, page)
            if(response.isSuccessful && response.body()!!.check){
                newItemCount = response.body()!!.information.dataEntity.size
                numRating = response.body()!!.information.numRating
                val data = response.body()!!.information.dataEntity.listIterator()
                while (data.hasNext()){
                    val temp = data.next()
                    list.add(GetMyRatingsDataEntity(
                        ratingId = temp.ratingId,
                        exhibitionId = temp.exhibitionId,
                        exhibitionName = "[${temp.exhibitionName}]",
                        rate = temp.rate)
                    )
                }
            } else {
                isFinished = true
            }

            page++
        }
    }
}