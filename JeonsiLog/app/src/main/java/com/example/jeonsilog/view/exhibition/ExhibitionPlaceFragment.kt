package com.example.jeonsilog.view.exhibition

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.place.GetPlacesInformationEntity
import com.example.jeonsilog.databinding.FragmentExhibitionPlaceBinding
import com.example.jeonsilog.repository.place.PlaceRepositoryImpl
import com.example.jeonsilog.viewmodel.ExhibitionViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.extraActivityReference
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.newPlaceId
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.newPlaceName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class ExhibitionPlaceFragment : BaseFragment<FragmentExhibitionPlaceBinding>(
    R.layout.fragment_exhibition_place) {
    private lateinit var placeList: MutableList<GetPlacesInformationEntity>
    private lateinit var exhibitionPlaceRvAdapter: ExhibitionPlaceRvAdapter
    private val exhibitionViewModel: ExhibitionViewModel by activityViewModels()

    private var placePage = 0
    private var placeId = 0
    private var hasNextPage = true

    override fun init() {
        GlobalApplication.isRefresh.observe(this){
            if(it){
                (activity as ExtraActivity).refreshFragment(R.id.exhibitionFragment)
                GlobalApplication.isRefresh.value = false
            }
        }

        placePage = 0
        placeId = newPlaceId
        binding.tvToolBarTitle.text = newPlaceName
        placeList = mutableListOf()

        exhibitionPlaceRvAdapter = ExhibitionPlaceRvAdapter(placeList, requireContext())
        binding.rvExhibition.adapter = exhibitionPlaceRvAdapter
        binding.rvExhibition.layoutManager = LinearLayoutManager(this.context)

        exhibitionPlaceRvAdapter.setOnItemClickListener(object :ExhibitionPlaceRvAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: GetPlacesInformationEntity, position: Int) {
                if(exhibitionViewModel.currentExhibitionIds.value==null){
                    exhibitionViewModel.setCurrentExhibitionIds(data.exhibitionId)
                    Log.d("TAG", "onItemClick: current exhibiton null")
                }else{
                    exhibitionViewModel.addCurrentExhibitionId(data.exhibitionId)
                    Log.d("TAG", "onItemClick: add current exhibiton")
                }
                Log.d("TAG", "place -> exhibition: ${exhibitionViewModel.currentExhibitionIds}")
                Navigation.findNavController(v).navigate(R.id.action_exhibitionPlaceFragment_to_exhibitionFragment)
            }

        })

        setExhibitionRvByPage(0)

        //recyclerView 페이징 처리
        binding.rvExhibition.addOnScrollListener(object : RecyclerView.OnScrollListener(){
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

    private fun setExhibitionRvByPage(totalCount:Int){
        var addItemCount = 0
        runBlocking(Dispatchers.IO) {
            val response = PlaceRepositoryImpl().getPlaces(encryptedPrefs.getAT(),placeId,placePage)
            if(response.isSuccessful && response.body()!!.check){
                placeList.addAll(response.body()!!.informationEntity.data)
                addItemCount = response.body()!!.informationEntity.data.size
                hasNextPage = response.body()!!.informationEntity.hasNextPage
            }
        }
        val startPosition = totalCount + 1
        exhibitionPlaceRvAdapter.notifyItemRangeInserted(startPosition, addItemCount)
        placePage++
    }

    //Back Button 눌렀을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("TAG", "handleOnBackPressed: extraActivityReference: $extraActivityReference ")
                if(extraActivityReference ==3 && (exhibitionViewModel.currentExhibitionIds.value == null || exhibitionViewModel.currentExhibitionIds.value?.size!! <1)){
                    activity?.finish()
                }else{
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}