package com.example.jeonsilog.view.otheruser

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.follow.GetOtherFollowerEntity
import com.example.jeonsilog.data.remote.dto.follow.GetOtherFollowerInformation
import com.example.jeonsilog.databinding.FragmentOtherUserListFollowerBinding
import com.example.jeonsilog.repository.follow.FollowRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFollowerUpdate
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFollowingUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class OtherUserListFollowerFragment(private val otherUserId: Int): BaseFragment<FragmentOtherUserListFollowerBinding>(R.layout.fragment_other_user_list_follower) {
    private val list = mutableListOf<GetOtherFollowerEntity>()
    private lateinit var adapter: OtherUserListRvAdapter<GetOtherFollowerEntity>
    private var page = 0
    private var isFinished = false
    private var newItemCount = 0

    override fun init() {
        adapter = OtherUserListRvAdapter(list, 0, requireContext())
        binding.rvOtherUserFollower.adapter = adapter
        binding.rvOtherUserFollower.layoutManager = LinearLayoutManager(requireContext())
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
            val response = FollowRepositoryImpl().getOtherFollower(encryptedPrefs.getAT(), otherUserId, page)
            if(response.isSuccessful && response.body()!!.check){
                newItemCount = response.body()!!.information.data.size
                val data = response.body()!!.information.data.listIterator()
                while (data.hasNext()){
                    list.add(data.next())
                }
            } else {
                isFinished = true
            }

            page++
        }
    }

    private fun emptyView(){
        if(list.isEmpty()){
            binding.rvOtherUserFollower.visibility = View.GONE
            binding.ivOtherUserListFollowerEmptyImg.visibility = View.VISIBLE
            binding.tvOtherUserListFollowerEmptyTitle.visibility = View.VISIBLE
        } else {
            binding.rvOtherUserFollower.visibility = View.VISIBLE
            binding.ivOtherUserListFollowerEmptyImg.visibility = View.GONE
            binding.tvOtherUserListFollowerEmptyTitle.visibility = View.GONE

            binding.rvOtherUserFollower.addOnScrollListener(object: RecyclerView.OnScrollListener(){
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