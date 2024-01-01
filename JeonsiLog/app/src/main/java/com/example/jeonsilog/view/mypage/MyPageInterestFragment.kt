package com.example.jeonsilog.view.mypage

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun init() {
        runBlocking(Dispatchers.IO){
            val response = InterestRepositoryImpl().getInterest(GlobalApplication.encryptedPrefs.getAT())
            if(response.isSuccessful && response.body()!!.check){
                val data = response.body()!!.information.listIterator()
                while (data.hasNext()){
                    list.add(data.next())
                }
            }
        }

        if(list.isEmpty()){
            binding.rvMypageInterest.visibility = View.GONE
            binding.ivMypageInterestEmptyImg.visibility = View.VISIBLE
            binding.tvMypageInterestEmptyTitle.visibility = View.VISIBLE
            binding.tvMypageReviewEmptyDescription.visibility = View.VISIBLE
        } else {
            val adapter = MyPageRvAdapter<GetInterestInformationEntity>(list, 2)
            binding.rvMypageInterest.adapter = adapter
            binding.rvMypageInterest.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}