package com.example.jeonsilog.view.mypage

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.follow.GetMyFollowerEntity
import com.example.jeonsilog.databinding.FragmentMyPageListFollowerBinding
import com.example.jeonsilog.repository.follow.FollowRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFollowerUpdate
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFollowingUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MyPageListFollowerFragment: BaseFragment<FragmentMyPageListFollowerBinding>(R.layout.fragment_my_page_list_follower) {
    private val list = mutableListOf<GetMyFollowerEntity>()
    private lateinit var adapter: MyPageListRvAdapter<GetMyFollowerEntity>
    private var newItemCount = 0
    private var isFinished = false
    private var page = 0

    override fun init() {
        adapter = MyPageListRvAdapter(list, 0, requireContext(), null)
        binding.rvMypageFollower.adapter = adapter
        binding.rvMypageFollower.layoutManager = LinearLayoutManager(requireContext())
        updateList()
        emptyView()


        isFollowingUpdate.observe(this){
            if(it){
                Log.d(tag, it.toString())
                list.clear()
                page = 0
                isFinished = false

                updateList()
                isFollowingUpdate.value = false
                emptyView()

                adapter.notifyDataSetChanged()
            }
        }

        isFollowerUpdate.observe(this){
            if(it){
                emptyView()
            }
        }
    }

    private fun updateList(){
        runBlocking(Dispatchers.IO){
            val response = FollowRepositoryImpl().getMyFollower(encryptedPrefs.getAT(), page)
            if(response.isSuccessful && response.body()!!.check){
                newItemCount = response.body()!!.information.data.size
                val data = response.body()!!.information.data.listIterator()
                while (data.hasNext()){
                    list.add(data.next())
                }
            } else {
                isFinished = true
            }
            page ++
        }
    }

    private fun emptyView(){
        if(list.isEmpty()){
            binding.rvMypageFollower.visibility = View.GONE
            binding.ivMypageListFollowerEmptyImg.visibility = View.VISIBLE
            binding.tvMypageListFollowerEmptyTitle.visibility = View.VISIBLE
        } else {
            binding.rvMypageFollower.visibility = View.VISIBLE
            binding.ivMypageListFollowerEmptyImg.visibility = View.GONE
            binding.tvMypageListFollowerEmptyTitle.visibility = View.GONE

            binding.rvMypageFollower.addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    val totalCount = recyclerView.adapter?.itemCount?.minus(1)

                    if(totalCount == rvPosition){
                        if(!isFinished){
                            updateList()

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
}