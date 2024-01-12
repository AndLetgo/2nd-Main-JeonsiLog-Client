package com.example.jeonsilog.view.home

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.exhibition.ExhibitionsInfo
import com.example.jeonsilog.databinding.FragmentHomeBinding
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.view.exhibition.ExhibitionFragment
import com.example.jeonsilog.view.mypage.MyPageFragment
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isRefresh
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private lateinit var homeRvAdapter: HomeRvAdapter
    private var homeRvList = mutableListOf<ExhibitionsInfo>()
    private var exhibitionPage = 0
    private var hasNextPage = true

    override fun init() {

        homeRvAdapter = HomeRvAdapter(homeRvList, requireContext())
        binding.rvHomeExhibition.adapter = homeRvAdapter
        binding.rvHomeExhibition.layoutManager = LinearLayoutManager(this.context)

        homeRvAdapter.setOnItemClickListener(object : HomeRvAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: ExhibitionsInfo, position: Int) {
                Log.d("exhibitoinId", "onItemClick: exhibitionID: ${data.exhibitionId}")
                (activity as MainActivity).loadExtraActivity(0, data.exhibitionId)
            }
        })

        setExhibitionRvByPage(0)

        //recyclerView 페이징 처리
        binding.rvHomeExhibition.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = recyclerView.adapter?.itemCount?.minus(2)
                if(rvPosition == totalCount && hasNextPage){
                    setExhibitionRvByPage(totalCount)
                }
            }
        })

        binding.ibFabTop.setOnClickListener {
            binding.rvHomeExhibition.smoothScrollToPosition(0)
        }
        binding.toolbar.setOnClickListener {
            binding.rvHomeExhibition.smoothScrollToPosition(0)
        }
    }

    //recyclerView 페이징
    private fun setExhibitionRvByPage(totalCount:Int){
        var addItemCount = 0
        runBlocking(Dispatchers.IO) {
            val response = ExhibitionRepositoryImpl().getExhibitions(encryptedPrefs.getAT(),exhibitionPage)
            if(response.isSuccessful && response.body()!!.check ){
                homeRvList.addAll(response.body()!!.information.data)
                addItemCount = response.body()!!.information.data.size
                hasNextPage = response.body()!!.information.hasNextPage
            }
        }
        val startPosition = totalCount + 1
        homeRvAdapter.notifyItemRangeInserted(startPosition, addItemCount)
        exhibitionPage++
    }

}