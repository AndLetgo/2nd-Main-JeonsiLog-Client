package com.example.jeonsilog.view.search

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private lateinit var exhibitionPlaceItemAdapter: ExhibitionPlaceItemAdapter
    val exhibitionPlaceRvList= mutableListOf<SearchPlacesInformationEntity>()
    var itemPage=0
    var hasNextPage=true

    lateinit var viewModel: SearchViewModel
    override fun init() {
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        exhibitionPlaceItemAdapter = ExhibitionPlaceItemAdapter(requireContext(),exhibitionPlaceRvList)
        binding.rvExhibitionplace.adapter = exhibitionPlaceItemAdapter
        binding.rvExhibitionplace.layoutManager = LinearLayoutManager(requireContext())

        exhibitionPlaceItemAdapter?.setOnItemClickListener(object : ExhibitionPlaceItemAdapter.OnItemClickListener{
            override fun onItemClick(type: Int, placeItem: SearchPlacesInformationEntity) {
                extraActivityReference = type
                newPlaceId = placeItem.placeId
                newPlaceName = placeItem.placeName
                val intent = Intent(requireContext(), ExtraActivity::class.java)
                startActivity(intent)
            }
        })
        itemPage=0
        setExhibitionPlaceRvByPage(0)

        binding.rvExhibitionplace.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = recyclerView.adapter?.itemCount?.minus(2)
                if(rvPosition == totalCount && hasNextPage){
                    setExhibitionPlaceRvByPage(totalCount)
                }
            }
        })
    }
    fun checkEmptyListTrue(){
        binding.ivEmpty.isGone=true
        binding.tvEmpty01.isGone=true
        binding.tvEmpty02.isGone=true
        Log.d("setExhibitionPlaceRBPP", "CheckEmptyListTrue: ")
    }
    fun checkEmptyListFalse(){
        binding.ivEmpty.isGone=false
        binding.tvEmpty01.isGone=false
        binding.tvEmpty02.isGone=false
        Log.d("setExhibitionPlaceRBPP", "CheckEmptyListFalse: ")
    }

    private fun setExhibitionPlaceRvByPage(totalCount:Int){
        var addItemCount = 0
        runBlocking(Dispatchers.IO) {
            Log.d("setExhibitionPlaceRBPP", "Edittext==$edittext||ItemPage==$itemPage ")
            val response = PlaceRepositoryImpl().searchPlaces(encryptedPrefs.getAT(),edittext,itemPage)
            Log.d("setExhibitionPlaceRBPP", "$response: ")
            if(response.isSuccessful && response.body()!!.check){
                if(itemPage==0){
                    exhibitionPlaceRvList.clear()
                }
                exhibitionPlaceRvList.addAll(response.body()!!.information.data.toMutableList())
                addItemCount = response.body()!!.information.data.size
                hasNextPage = response.body()!!.information.hasNextPage
                checkEmptyListTrue()
            }else{
                checkEmptyListFalse()
            }
        }

        val startPosition = totalCount + 1
        exhibitionPlaceItemAdapter.notifyItemRangeInserted(startPosition,addItemCount)
        itemPage++

    }

}