package com.example.jeonsilog.view.otheruser

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.follow.GetOtherFollowingEntity
import com.example.jeonsilog.databinding.FragmentOtherUserListFollowingBinding
import com.example.jeonsilog.repository.follow.FollowRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFollowerUpdate
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFollowingUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class OtherUserListFollowingFragment(private val otherUserId: Int): BaseFragment<FragmentOtherUserListFollowingBinding>(R.layout.fragment_other_user_list_following) {
    private val list = mutableListOf<GetOtherFollowingEntity>()
    private lateinit var adapter: OtherUserListRvAdapter<GetOtherFollowingEntity>
    private var page = 0
    private var isFinished = false
    private var newItemCount = 0

    override fun init() {
        adapter = OtherUserListRvAdapter(list, 1, requireContext())
        binding.rvOtherUserFollowing.adapter = adapter
        binding.rvOtherUserFollowing.layoutManager = LinearLayoutManager(requireContext())

        updateList()
        emptyView()

        isFollowerUpdate.observe(this){
            if(it){
                Log.d(tag, it.toString())
                list.clear()
                page = 0
                isFinished = false

                updateList()
                isFollowerUpdate.value = false
                emptyView()

                adapter.notifyDataSetChanged()
            }
        }

        isFollowingUpdate.observe(this){
            if(it){
                emptyView()
            }
        }
    }

    private fun updateList(){
        runBlocking(Dispatchers.IO){
            val response = FollowRepositoryImpl().getOtherFollowing(GlobalApplication.encryptedPrefs.getAT(), otherUserId, page)
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
            binding.rvOtherUserFollowing.visibility = View.GONE
            binding.ivOtherUserListFollowingEmptyImg.visibility = View.VISIBLE
            binding.tvOtherUserListFollowingEmptyTitle.visibility = View.VISIBLE
            binding.tvOtherUserListFollowingEmptyDescription.visibility = View.VISIBLE
        } else {
            binding.rvOtherUserFollowing.visibility = View.VISIBLE
            binding.ivOtherUserListFollowingEmptyImg.visibility = View.GONE
            binding.tvOtherUserListFollowingEmptyTitle.visibility = View.GONE
            binding.tvOtherUserListFollowingEmptyDescription.visibility = View.GONE

            binding.rvOtherUserFollowing.addOnScrollListener(object: RecyclerView.OnScrollListener(){
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