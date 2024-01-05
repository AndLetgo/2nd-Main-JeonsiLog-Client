package com.example.jeonsilog.view.otheruser

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.follow.GetOtherFollowingInformation
import com.example.jeonsilog.databinding.FragmentOtherUserListFollowingBinding
import com.example.jeonsilog.repository.follow.FollowRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFollowerUpdate
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFollowingUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class OtherUserListFollowingFragment(private val otherUserId: Int): BaseFragment<FragmentOtherUserListFollowingBinding>(R.layout.fragment_other_user_list_following) {
    private val list = mutableListOf<GetOtherFollowingInformation>()
    private lateinit var adapter: OtherUserListRvAdapter<GetOtherFollowingInformation>
    override fun init() {
        adapter = OtherUserListRvAdapter(list, 1, requireContext())
        binding.rvOtherUserFollowing.adapter = adapter
        binding.rvOtherUserFollowing.layoutManager = LinearLayoutManager(requireContext())

        updateList()
        emptyView()

        isFollowerUpdate.observe(this){
            if(it){
                Log.d(tag, it.toString())
                updateList()
                isFollowerUpdate.value = false
                emptyView()
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
            list.clear()
            val response = FollowRepositoryImpl().getOtherFollowing(GlobalApplication.encryptedPrefs.getAT(), otherUserId)
            if(response.isSuccessful && response.body()!!.check){
                val data = response.body()!!.information.listIterator()
                while (data.hasNext()){
                    list.add(data.next())
                }
            }
        }
        adapter.notifyDataSetChanged()
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
        }
    }
}