package com.example.jeonsilog.view.home


import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.exhibition.ExhibitionsInfo
import com.example.jeonsilog.databinding.FragmentHomeBinding
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.viewmodel.AdminViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.exhibitionId
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isAdminExhibitionOpen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val adminViewModel: AdminViewModel by activityViewModels()
    private lateinit var homeRecentlyRvAdapter: HomeRecentlyRvAdapter
    private lateinit var homeColorfulRvAdapter: HomeColorfulRvAdapter
    private lateinit var homeEndSoonRvAdapter: HomeEndSoonRvAdapter
    private lateinit var homeNewStartRvAdapter: HomeNewStartRvAdapter

    private var homeRecentlyRvList = mutableListOf<ExhibitionsInfo>()
    private var homeColorfulRvList = mutableListOf<ExhibitionsInfo>()
    private var homeEndSoonRvList = mutableListOf<ExhibitionsInfo>()
    private var homeNewStartRvList = mutableListOf<ExhibitionsInfo>()

//    private var exhibitionPage = 0
    val TAG = "homeTest"

    override fun init() {
        adminViewModel.resetExhibitionInfo()
        if(adminViewModel.isChanged.value!!){
            (activity as MainActivity).refreshFragmentInAdmin(R.id.homeFragment)
            adminViewModel.setIsChanged(false)
        }
        isAdminExhibitionOpen = false
        //관리자 체크
        if(encryptedPrefs.getCheckAdmin()){
            binding.btnChangeAdminUser.visibility = View.VISIBLE
        }else{
            binding.btnChangeAdminUser.visibility = View.GONE
        }
        if(adminViewModel.isAdminPage.value!!){
//            binding.ibFabTop.visibility = View.GONE
//            binding.ibFabTopAdmin.visibility = View.VISIBLE
            binding.btnChangeAdminUser.text = getString(R.string.btn_change_to_user)
            (activity as MainActivity).setStateBn(true, "admin")
        }else{
//            binding.ibFabTop.visibility = View.VISIBLE
//            binding.ibFabTopAdmin.visibility = View.GONE
            binding.btnChangeAdminUser.text = getString(R.string.btn_change_to_admin)
            (activity as MainActivity).setStateBn(true, "user")
        }

        homeRecentlyRvAdapter = HomeRecentlyRvAdapter(homeColorfulRvList, requireContext())
        binding.rvPopular.adapter = homeRecentlyRvAdapter
        binding.rvPopular.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        homeColorfulRvAdapter = HomeColorfulRvAdapter(homeColorfulRvList, requireContext())
        binding.rvArtistic.adapter = homeColorfulRvAdapter
        binding.rvArtistic.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        homeEndSoonRvAdapter = HomeEndSoonRvAdapter(homeEndSoonRvList, requireContext())
        binding.rvEndSoon.adapter = homeEndSoonRvAdapter
        binding.rvEndSoon.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        homeNewStartRvAdapter = HomeNewStartRvAdapter(homeNewStartRvList, requireContext())
        binding.rvNewStart.adapter = homeNewStartRvAdapter
        binding.rvNewStart.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        homeRecentlyRvAdapter.setOnItemClickListener(object : HomeRecentlyRvAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: ExhibitionsInfo, position: Int) {
                moveExhibitionDetail(v, data)
            }
        })
        homeColorfulRvAdapter.setOnItemClickListener(object :HomeColorfulRvAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: ExhibitionsInfo, position: Int) {
                moveExhibitionDetail(v, data)
            }
        })
        homeEndSoonRvAdapter.setOnItemClickListener(object :HomeEndSoonRvAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: ExhibitionsInfo, position: Int) {
                moveExhibitionDetail(v, data)
            }
        })
        homeNewStartRvAdapter.setOnItemClickListener(object :HomeNewStartRvAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: ExhibitionsInfo, position: Int) {
                moveExhibitionDetail(v, data)
            }
        })

        getRvData()

        binding.btnChangeAdminUser.setOnClickListener {
            when(binding.btnChangeAdminUser.text){
                getString(R.string.btn_change_to_admin) -> {
                    (activity as MainActivity).checkAdmin(true)
                }
                getString(R.string.btn_change_to_user) -> {
                    (activity as MainActivity).checkAdmin(false)
                }
            }
        }
    }

    private fun getRvData(){
        runBlocking(Dispatchers.IO) {
            val response = ExhibitionRepositoryImpl().getExhibitionsRecently(encryptedPrefs.getAT())
            if(response.isSuccessful && response.body()!!.check ){
                homeRecentlyRvList.addAll(response.body()!!.information.data)
                Log.d(TAG, "getRvData: recently")
            }
        }
        runBlocking(Dispatchers.IO) {
            val response = ExhibitionRepositoryImpl().getExhibitionsColorful(encryptedPrefs.getAT())
            if(response.isSuccessful && response.body()!!.check ){
                homeColorfulRvList.addAll(response.body()!!.information)
                Log.d(TAG, "getRvData: colorful")
            }
        }
        runBlocking(Dispatchers.IO) {
            val response = ExhibitionRepositoryImpl().getExhibitionsEndingSoon(encryptedPrefs.getAT())
            if(response.isSuccessful && response.body()!!.check ){
                homeEndSoonRvList.addAll(response.body()!!.information)
                Log.d(TAG, "getRvData: endingSoon")
            }
        }
        runBlocking(Dispatchers.IO) {
            val response = ExhibitionRepositoryImpl().getExhibitionsNew(encryptedPrefs.getAT())
            if(response.isSuccessful && response.body()!!.check ){
                homeNewStartRvList.addAll(response.body()!!.information)
                Log.d(TAG, "getRvData: newStartt")
            }
        }
    }

    private fun moveExhibitionDetail(v: View, data: ExhibitionsInfo){
        //관리자 체크
        if(adminViewModel.isAdminPage.value!!){
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_adminExhibitionFragment)
            exhibitionId = data.exhibitionId
            isAdminExhibitionOpen = true
        }else{
            (activity as MainActivity).loadExtraActivity(0, data.exhibitionId)
            Log.d("home", "onItemClick: ${data.exhibitionId}")
        }
    }
}