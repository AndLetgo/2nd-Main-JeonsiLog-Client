package com.example.jeonsilog.view.mypage

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.interest.GetInterestInformationEntity
import com.example.jeonsilog.databinding.FragmentMyPageInterestBinding
import com.example.jeonsilog.repository.interest.InterestRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MyPageInterestFragment:BaseFragment<FragmentMyPageInterestBinding>(R.layout.fragment_my_page_interest) {
    var list = mutableListOf<GetInterestInformationEntity>()
    private var page = 0
    private var isFinished = false
    private lateinit var adapter: MyPageRvAdapter<GetInterestInformationEntity>
    private var newItemCount = 0

    override fun init() {
        getItems()

        if(list.isEmpty()){
            binding.rvMypageInterest.visibility = View.GONE
            binding.ivMypageInterestEmptyImg.visibility = View.VISIBLE
            binding.tvMypageInterestEmptyTitle.visibility = View.VISIBLE
            binding.tvMypageReviewEmptyDescription.visibility = View.VISIBLE
        } else {
            adapter = MyPageRvAdapter<GetInterestInformationEntity>(list, 2, requireContext())
            binding.rvMypageInterest.adapter = adapter
            binding.rvMypageInterest.layoutManager = LinearLayoutManager(requireContext())

            binding.rvMypageInterest.addOnScrollListener(object: RecyclerView.OnScrollListener(){
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
            val response = InterestRepositoryImpl().getInterest(GlobalApplication.encryptedPrefs.getAT(), page)
            if(response.isSuccessful && response.body()!!.check){
                newItemCount = response.body()!!.information.size
                val data = response.body()!!.information.listIterator()
                while (data.hasNext()){
                    list.add(data.next())
                }
            } else {
                isFinished = true
            }

            page++
        }
    }
}