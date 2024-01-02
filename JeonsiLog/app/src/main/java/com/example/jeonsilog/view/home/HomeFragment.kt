package com.example.jeonsilog.view.home

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.exhibition.ExhibitionInfo
import com.example.jeonsilog.data.remote.dto.exhibition.ExhibitionsInfo
import com.example.jeonsilog.databinding.FragmentHomeBinding
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.viewmodel.ExhibitionModel
import com.example.jeonsilog.viewmodel.HomeRvModel
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private lateinit var homeRvAdapter: HomeRvAdapter
    private var homeRvList = listOf<ExhibitionsInfo>()
    override fun init() {
        runBlocking(Dispatchers.IO) {
            val response = ExhibitionRepositoryImpl().getExhibitions(encryptedPrefs.getAT(),0)
            if(response.isSuccessful && response.body()!!.check){
                homeRvList = response.body()!!.informationEntity
            }
        }
        homeRvAdapter = HomeRvAdapter(homeRvList)
        binding.rvHomeExhibition.adapter = homeRvAdapter
        binding.rvHomeExhibition.layoutManager = LinearLayoutManager(this.context)

        homeRvAdapter.setOnItemClickListener(object : HomeRvAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: ExhibitionsInfo, position: Int) {
                (activity as MainActivity).loadExtraActivity(0)
            }
        })

        binding.ibFabTop.setOnClickListener {
            binding.rvHomeExhibition.scrollToPosition(0)
        }
    }

}