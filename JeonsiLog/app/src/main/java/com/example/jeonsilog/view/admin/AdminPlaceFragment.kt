package com.example.jeonsilog.view.admin

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.place.GetPlacesInformationEntity
import com.example.jeonsilog.databinding.FragmentExhibitionPlaceBinding
import com.example.jeonsilog.repository.place.PlaceRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.view.exhibition.ExhibitionPlaceRvAdapter
import com.example.jeonsilog.viewmodel.AdminViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class AdminPlaceFragment(private val placeId: Int, private val placeName: String): BaseFragment<FragmentExhibitionPlaceBinding>(R.layout.fragment_exhibition_place) {
    private val adminViewModel: AdminViewModel by activityViewModels()
    private lateinit var placeList: MutableList<GetPlacesInformationEntity>
    private lateinit var adapter: ExhibitionPlaceRvAdapter

    private var nextPage = 0
    private var hasNextPage = true

    override fun init() {
        GlobalApplication.isRefresh.observe(this){
            if(it){
                (requireActivity() as MainActivity).refreshFragment(AdminPlaceFragment(placeId, placeName))
                GlobalApplication.isRefresh.value = false
            }
        }

        binding.tvToolBarTitle.text = insertNewLine(placeName)
        placeList = mutableListOf()

        adapter = ExhibitionPlaceRvAdapter(placeList, requireContext())
        binding.rvExhibition.adapter = adapter
        binding.rvExhibition.layoutManager = LinearLayoutManager(requireContext())

        adapter.setOnItemClickListener(object: ExhibitionPlaceRvAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: GetPlacesInformationEntity, position: Int) {
                val navController = findNavController()
                navController.navigate(R.id.adminExhibitionFragment)
                (activity as MainActivity).setStateFcm(true)
                GlobalApplication.isAdminExhibitionOpen =true
                adminViewModel.setIsReport(false)
                GlobalApplication.exhibitionId = data.exhibitionId
            }
        })

        setExhibitionRvByPage(0)

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
            val response = PlaceRepositoryImpl().getPlaces(GlobalApplication.encryptedPrefs.getAT(), placeId, nextPage)
            if(response.isSuccessful && response.body()!!.check){
                placeList.addAll(response.body()!!.informationEntity.data)
                addItemCount = response.body()!!.informationEntity.data.size
                hasNextPage = response.body()!!.informationEntity.hasNextPage
            }
        }
        val startPosition = totalCount + 1
        adapter.notifyItemRangeInserted(startPosition, addItemCount)
        nextPage++
    }

    private fun insertNewLine(input: String): String {
        val index = input.indexOf('(')

        return if (index != -1) {
            val modified = StringBuilder(input)
            modified.insert(index, "\n")
            modified.toString()
        } else {
            input
        }
    }
}