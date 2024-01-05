package com.example.jeonsilog.view.otheruser

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.follow.GetOtherFollowerInformation
import com.example.jeonsilog.databinding.FragmentOtherUserListFollowerBinding
import com.example.jeonsilog.repository.follow.FollowRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFollowerUpdate
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFollowingUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class OtherUserListFollowerFragment(private val otherUserId: Int): BaseFragment<FragmentOtherUserListFollowerBinding>(R.layout.fragment_other_user_list_follower) {
    private val list = mutableListOf<GetOtherFollowerInformation>()
    private lateinit var adapter: OtherUserListRvAdapter<GetOtherFollowerInformation>

    override fun init() {
        adapter = OtherUserListRvAdapter(list, 0, requireContext())
        binding.rvOtherUserFollower.adapter = adapter
        binding.rvOtherUserFollower.layoutManager = LinearLayoutManager(requireContext())
        updateList()
        emptyView()


        isFollowingUpdate.observe(this){
            if(it){
                Log.d(tag, it.toString())
                updateList()
                isFollowerUpdate.value = false
                emptyView()
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
            list.clear()
            val response = FollowRepositoryImpl().getOtherFollower(encryptedPrefs.getAT(), otherUserId)
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
            binding.rvOtherUserFollower.visibility = View.GONE
            binding.ivOtherUserListFollowerEmptyImg.visibility = View.VISIBLE
            binding.tvOtherUserListFollowerEmptyTitle.visibility = View.VISIBLE
        } else {
            binding.rvOtherUserFollower.visibility = View.VISIBLE
            binding.ivOtherUserListFollowerEmptyImg.visibility = View.GONE
            binding.tvOtherUserListFollowerEmptyTitle.visibility = View.GONE
        }
    }
}