package com.example.jeonsilog.view.exhibition

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.place.GetPlacesInformationEntity
import com.example.jeonsilog.databinding.FragmentExhibitionPlaceBinding
import com.example.jeonsilog.repository.place.PlaceRepositoryImpl
import com.example.jeonsilog.viewmodel.ExhibitionViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class ExhibitionPlaceFragment : BaseFragment<FragmentExhibitionPlaceBinding>(
    R.layout.fragment_exhibition_place) {
    private lateinit var placeList: MutableList<GetPlacesInformationEntity>
    private lateinit var exhibitionPlaceRvAdapter: ExhibitionPlaceRvAdapter
    private val exhibitionViewModel: ExhibitionViewModel by activityViewModels()

    private var placePage = 0
    private var placeId = 0

    override fun init() {
        placePage = 0
        placeId = requireArguments().getInt("placeId")
        Log.d("place", "init: placeId: $placeId")
        binding.tvToolBarTitle.text = requireArguments().getString("placeName")
        placeList = mutableListOf()

        exhibitionPlaceRvAdapter = ExhibitionPlaceRvAdapter(placeList, requireContext())
        binding.rvExhibition.adapter = exhibitionPlaceRvAdapter
        binding.rvExhibition.layoutManager = LinearLayoutManager(this.context)

        exhibitionPlaceRvAdapter.setOnItemClickListener(object :ExhibitionPlaceRvAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: GetPlacesInformationEntity, position: Int) {
//                val navController = findNavController(ExtraActivity(),R.id.fcv_nav_frame)
//                val bundle = Bundle()
//                bundle.putInt("exhibitionId",data.exhibitionId)
                exhibitionViewModel.setCurrentExhibitionId(data.exhibitionId)
                Navigation.findNavController(v).navigate(R.id.action_exhibitionPlaceFragment_to_exhibitionFragment)
//                navController.navigate(R.id.exhibitionFragment)
            }

        })

        setExhibitionRvByPage(0)

        //recyclerView 페이징 처리
        binding.rvExhibition.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = recyclerView.adapter?.itemCount?.minus(2)
                if(rvPosition == totalCount){
                    setExhibitionRvByPage(totalCount)
                }
            }
        })
    }

    private fun setExhibitionRvByPage(totalCount:Int){
        var addItemCount = 0
        runBlocking(Dispatchers.IO) {
            val response = PlaceRepositoryImpl().getPlaces(encryptedPrefs.getAT(),placeId,placePage)
            if(response.isSuccessful && response.body()!!.check){
                placeList.addAll(response.body()!!.informationEntity)
                addItemCount = response.body()!!.informationEntity.size
            }
        }
        val startPosition = totalCount + 1
        exhibitionPlaceRvAdapter.notifyItemRangeInserted(startPosition, addItemCount)
        placePage++
    }
}