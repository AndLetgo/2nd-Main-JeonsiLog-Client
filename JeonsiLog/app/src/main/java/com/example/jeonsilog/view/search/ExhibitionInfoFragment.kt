package com.example.jeonsilog.view.search

import android.util.Log
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.ExhibitionInfoItem
import com.example.jeonsilog.data.remote.dto.ExhibitionRandom
import com.example.jeonsilog.data.remote.dto.exhibition.SearchInformationEntity
import com.example.jeonsilog.viewmodel.SearchViewModel
import com.example.jeonsilog.databinding.FragmentExihibitionInfoBinding
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.view.home.HomeRvAdapter
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ExhibitionInfoFragment(private val edittext:String): BaseFragment<FragmentExihibitionInfoBinding>(R.layout.fragment_exihibition_info){

    private lateinit var exhibitionInfoItemAdapter: ExhibitionInfoItemAdapter
    val exhibitionInfoRvList= mutableListOf<SearchInformationEntity>()
    var itemPage=0
    var hasNextPage=true

    lateinit var viewModel: SearchViewModel
    override fun init() {
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        exhibitionInfoItemAdapter = ExhibitionInfoItemAdapter(requireContext(),exhibitionInfoRvList)
        binding.rvExhibitioninfo.adapter = exhibitionInfoItemAdapter
        binding.rvExhibitioninfo.layoutManager = LinearLayoutManager(requireContext())
        itemPage=0
        setExhibitionRvByPage(0)



        binding.rvExhibitioninfo.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = recyclerView.adapter?.itemCount?.minus(2)
                if(rvPosition == totalCount && hasNextPage){
                    setExhibitionRvByPage(totalCount)
                }
            }
        })

    }
    fun checkEmptyListTrue(){
        binding.ivEmpty.isGone=true
        binding.tvEmpty01.isGone=true
        binding.tvEmpty02.isGone=true
    }
    fun checkEmptyListFalse(){
        binding.ivEmpty.isGone=false
        binding.tvEmpty01.isGone=false
        binding.tvEmpty02.isGone=false
    }


    private fun setExhibitionRvByPage(totalCount:Int){
        var addItemCount = 0
        runBlocking(Dispatchers.IO) {
            val response = ExhibitionRepositoryImpl().searchExhibition(encryptedPrefs.getAT(),edittext,itemPage)
            if(response.isSuccessful && response.body()!!.check){
                if(itemPage==0){
                    exhibitionInfoRvList.clear()
                }
                exhibitionInfoRvList.addAll(response.body()!!.information.data.toMutableList())
                addItemCount = response.body()!!.information.data.size
                hasNextPage = response.body()!!.information.hasNextPage
                checkEmptyListTrue()
            }else{
                checkEmptyListFalse()
            }
        }
        val startPosition = totalCount + 1
        exhibitionInfoItemAdapter.notifyItemRangeInserted(startPosition,addItemCount)
        itemPage++
    }
}