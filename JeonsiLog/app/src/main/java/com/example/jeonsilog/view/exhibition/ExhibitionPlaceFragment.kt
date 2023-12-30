package com.example.jeonsilog.view.exhibition

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentExhibitionPlaceBinding
import com.example.jeonsilog.viewmodel.ExhibitionModel

class ExhibitionPlaceFragment : BaseFragment<FragmentExhibitionPlaceBinding>(
    R.layout.fragment_exhibition_place) {
    private lateinit var exhibitionList: List<ExhibitionModel>
    private lateinit var exhibitionPlaceRvAdapter: ExhibitionPlaceRvAdapter


    override fun init() {
        exhibitionList = listOf(
            ExhibitionModel(0,"title","","","","","0000.00.00"),
            ExhibitionModel(1,"title","","","","","0000.00.00"),
            ExhibitionModel(2,"title","","","","","0000.00.00"),
            ExhibitionModel(3,"title","","","","","0000.00.00"),
            ExhibitionModel(4,"title","","","","","0000.00.00"),
            ExhibitionModel(5,"title","","","","","0000.00.00"),
            ExhibitionModel(6,"title","","","","","0000.00.00")
        )
        exhibitionPlaceRvAdapter = ExhibitionPlaceRvAdapter(exhibitionList)
        binding.rvExhibition.adapter = exhibitionPlaceRvAdapter
        binding.rvExhibition.layoutManager = LinearLayoutManager(this.context)
    }
}