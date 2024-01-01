package com.example.jeonsilog.view.mypage

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.follow.GetMyFollowingInformation
import com.example.jeonsilog.databinding.FragmentMyPageListFollowingBinding
import com.example.jeonsilog.repository.follow.FollowRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MyPageListFollowingFragment: BaseFragment<FragmentMyPageListFollowingBinding>(R.layout.fragment_my_page_list_following) {
    private val list = mutableListOf<GetMyFollowingInformation>()

    override fun init() {
        runBlocking(Dispatchers.IO){
            val response = FollowRepositoryImpl().getMyFollowing(GlobalApplication.encryptedPrefs.getAT())
            if(response.isSuccessful && response.body()!!.check){
                val data = response.body()!!.information.listIterator()
                while (data.hasNext()){
                    list.add(data.next())
                }
            }
        }


        if(list.isEmpty()){
            binding.rvMypageFollowing.visibility = View.GONE
            binding.ivMypageListFollowingEmptyImg.visibility = View.VISIBLE
            binding.tvMypageListFollowingEmptyTitle.visibility = View.VISIBLE
            binding.tvMypageListFollowingEmptyDescription.visibility = View.VISIBLE
        } else {
            val adapter = MyPageListRvAdapter<GetMyFollowingInformation>(list, 1, requireContext())
            binding.rvMypageFollowing.adapter = adapter
            binding.rvMypageFollowing.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}