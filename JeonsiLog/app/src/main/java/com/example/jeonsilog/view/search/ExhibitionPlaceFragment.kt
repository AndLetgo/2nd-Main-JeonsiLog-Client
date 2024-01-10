package com.example.jeonsilog.view.search

import android.content.Intent
import android.view.View
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.place.GetPlacesInformationEntity

import com.example.jeonsilog.data.remote.dto.place.SearchPlacesInformationEntity
import com.example.jeonsilog.databinding.FragmentSearchExhibitionPlaceBinding
import com.example.jeonsilog.repository.place.PlaceRepositoryImpl
import com.example.jeonsilog.view.exhibition.ExhibitionPlaceRvAdapter
import com.example.jeonsilog.view.exhibition.ExtraActivity
import com.example.jeonsilog.viewmodel.SearchViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.extraActivityReference
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.newPlaceId
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.newPlaceName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking


class ExhibitionPlaceFragment(private val edittext:String) : BaseFragment<FragmentSearchExhibitionPlaceBinding>(R.layout.fragment_search_exhibition_place) {
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
        //binding.ibFabTopExihibitionPlace.setOnClickListener {
        //    binding.rvExhibitionplace.scrollToPosition(0)
        //}
        //리사이클러뷰 제어
        binding.rvExhibitionplace.layoutManager = LinearLayoutManager(requireContext())
        var adapter: ExhibitionPlaceItemAdapter?
        var list: List<SearchPlacesInformationEntity>?
        runBlocking(Dispatchers.IO) {
            val response = PlaceRepositoryImpl().searchPlaces(encryptedPrefs.getAT(),edittext,0)
            if(response.isSuccessful && response.body()!!.check){
                val searchPlaceResponse = response.body()
                list=searchPlaceResponse?.informationEntity
            }
            else{
                val searchPlaceResponse = response.body()
                list=searchPlaceResponse?.informationEntity
            }
        }
        if (!list.isNullOrEmpty()){
            adapter = context?.let { ExhibitionPlaceItemAdapter(it,edittext,list!!.toMutableList()) }
            adapter?.setOnItemClickListener(object : ExhibitionPlaceItemAdapter.OnItemClickListener{
                override fun onItemClick(type: Int, placeItem: SearchPlacesInformationEntity) {
                    extraActivityReference = type
                    newPlaceId = placeItem.placeId
                    newPlaceName = placeItem.placeName
                    val intent = Intent(requireContext(), ExtraActivity::class.java)
                    startActivity(intent)
                }
            })
            binding.rvExhibitionplace.adapter = adapter
            checkEmptyListTrue()
        }
    }
}