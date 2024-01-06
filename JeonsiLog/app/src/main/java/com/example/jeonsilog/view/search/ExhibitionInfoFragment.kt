package com.example.jeonsilog.view.search

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
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ExhibitionInfoFragment(private val edittext:String): BaseFragment<FragmentExihibitionInfoBinding>(R.layout.fragment_exihibition_info){

    lateinit var viewModel: SearchViewModel
    override fun init() {
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        checkEmptyListFalse()
        setLayoutView()

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
    fun setLayoutView(){
        //binding.ibFabTopExihibitionInfo.setOnClickListener {
        //    binding.rvExhibitioninfo.scrollToPosition(0)
        //}
        //리사이클러뷰 제어
        binding.rvExhibitioninfo.layoutManager = LinearLayoutManager(requireContext())
        var adapter: ExhibitionInfoItemAdapter?
        var list: List<SearchInformationEntity>?
        runBlocking(Dispatchers.IO) {
            val response = ExhibitionRepositoryImpl().searchExhibition(encryptedPrefs.getAT(),edittext,0)
            if(response.isSuccessful && response.body()!!.check){
                val searchExhibitionResponse = response.body()
                list=searchExhibitionResponse?.informationEntity
            }
            else{
                val searchExhibitionResponse = response.body()
                list=searchExhibitionResponse?.informationEntity
                checkEmptyListFalse()
            }
        }
        if (!list.isNullOrEmpty()){
            adapter = context?.let { ExhibitionInfoItemAdapter(it,edittext,list!!.toMutableList()) }
            binding.rvExhibitioninfo.adapter = adapter
            checkEmptyListTrue()
        }
    }
}