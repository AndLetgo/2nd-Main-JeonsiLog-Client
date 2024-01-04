package com.example.jeonsilog.view.mypage

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.follow.GetOtherFollowingInformation
import com.example.jeonsilog.databinding.FragmentMyPageListFollowerBinding
import com.example.jeonsilog.repository.follow.FollowRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFollowerUpdate
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFollowingUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MyPageListFollowerFragment: BaseFragment<FragmentMyPageListFollowerBinding>(R.layout.fragment_my_page_list_follower) {
    private val list = mutableListOf<GetOtherFollowingInformation>()
    private lateinit var adapter: MyPageListRvAdapter<GetOtherFollowingInformation>

    override fun init() {
        adapter = MyPageListRvAdapter(list, 0, requireContext())
        binding.rvMypageFollower.adapter = adapter
        binding.rvMypageFollower.layoutManager = LinearLayoutManager(requireContext())
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
            val response = FollowRepositoryImpl().getMyFollower(encryptedPrefs.getAT())
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
            binding.rvMypageFollower.visibility = View.GONE
            binding.ivMypageListFollowerEmptyImg.visibility = View.VISIBLE
            binding.tvMypageListFollowerEmptyTitle.visibility = View.VISIBLE
        } else {
            binding.rvMypageFollower.visibility = View.VISIBLE
            binding.ivMypageListFollowerEmptyImg.visibility = View.GONE
            binding.tvMypageListFollowerEmptyTitle.visibility = View.GONE
        }
    }
}