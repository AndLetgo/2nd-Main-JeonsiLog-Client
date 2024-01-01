package com.example.jeonsilog.view.mypage

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.follow.GetMyFollowingInformation
import com.example.jeonsilog.databinding.FragmentMyPageListFollowingBinding
import com.example.jeonsilog.repository.follow.FollowRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFollowerUpdate
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isFollowingUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MyPageListFollowingFragment: BaseFragment<FragmentMyPageListFollowingBinding>(R.layout.fragment_my_page_list_following) {
    private val list = mutableListOf<GetMyFollowingInformation>()
    private lateinit var adapter: MyPageListRvAdapter<GetMyFollowingInformation>
    override fun init() {
        adapter = MyPageListRvAdapter(list, 1, requireContext())
        binding.rvMypageFollowing.adapter = adapter
        binding.rvMypageFollowing.layoutManager = LinearLayoutManager(requireContext())

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
            val response = FollowRepositoryImpl().getMyFollowing(GlobalApplication.encryptedPrefs.getAT())
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
            binding.rvMypageFollowing.visibility = View.GONE
            binding.ivMypageListFollowingEmptyImg.visibility = View.VISIBLE
            binding.tvMypageListFollowingEmptyTitle.visibility = View.VISIBLE
            binding.tvMypageListFollowingEmptyDescription.visibility = View.VISIBLE
        } else {
            binding.rvMypageFollowing.visibility = View.VISIBLE
            binding.ivMypageListFollowingEmptyImg.visibility = View.GONE
            binding.tvMypageListFollowingEmptyTitle.visibility = View.GONE
            binding.tvMypageListFollowingEmptyDescription.visibility = View.GONE
        }
    }
}