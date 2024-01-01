package com.example.jeonsilog.view.home

import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentHomeBinding
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.view.admin.AdminExhibitionFragment
import com.example.jeonsilog.viewmodel.ExhibitionModel
import com.example.jeonsilog.viewmodel.HomeRvModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private lateinit var homeRvAdapter: HomeRvAdapter
    override fun init() {
        (activity as MainActivity).setStateBn(true,"admin")

        val list = listOf<ExhibitionModel>(
            ExhibitionModel(0,"title","address","place","",""),
            ExhibitionModel(1,"title","address","place","",""),
            ExhibitionModel(2,"title","address","place","",""),
            ExhibitionModel(3,"title","address","place","",""),
            ExhibitionModel(4,"title","address","place","",""),
            ExhibitionModel(5,"title","address","place","",""),
            ExhibitionModel(6,"title","address","place","",""),
            ExhibitionModel(7,"title","address","place","",""),
            ExhibitionModel(8,"title","address","place","",""),
            ExhibitionModel(9,"title","address","place","",""),
            ExhibitionModel(10,"title","address","place","",""),
            ExhibitionModel(11,"title","address","place","","")
        )
        homeRvAdapter = HomeRvAdapter(list)
        binding.rvHomeExhibition.adapter = homeRvAdapter
        binding.rvHomeExhibition.layoutManager = LinearLayoutManager(this.context)

        homeRvAdapter.setOnItemClickListener(object : HomeRvAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: ExhibitionModel, position: Int) {
                //UserId check
//                (activity as MainActivity).replaceFragment(AdminExhibitionFragment())
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_adminExhibitionFragment)
                (activity as MainActivity).setStateBn(false,"admin")
            }
        })
    }

}