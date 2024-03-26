package com.example.jeonsilog.view.home


import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var homePopularRvAdapter: HomePopularRvAdapter
    private lateinit var homeArtisticRvAdapter: HomeArtisticRvAdapter
    private lateinit var homeEndSoonRvAdapter: HomeEndSoonRvAdapter
    private lateinit var homeNewStartRvAdapter: HomeNewStartRvAdapter

    private var homePopularRvList = mutableListOf<ExhibitionsInfo>()
    private var homeArtisticRvList = mutableListOf<ExhibitionsInfo>()
    private var homeEndSoonRvList = mutableListOf<ExhibitionsInfo>()
    private var homeNewStartRvList = mutableListOf<ExhibitionsInfo>()

//    private var exhibitionPage = 0
    private var hasNextPage = true

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

        homePopularRvAdapter = HomePopularRvAdapter(homePopularRvList, requireContext())
        binding.rvPopular.adapter = homePopularRvAdapter
        binding.rvPopular.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        homeArtisticRvAdapter = HomeArtisticRvAdapter(homeArtisticRvList, requireContext())
        binding.rvArtistic.adapter = homeArtisticRvAdapter
        binding.rvArtistic.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        homeEndSoonRvAdapter = HomeEndSoonRvAdapter(homeEndSoonRvList, requireContext())
        binding.rvEndSoon.adapter = homeEndSoonRvAdapter
        binding.rvEndSoon.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        homeNewStartRvAdapter = HomeNewStartRvAdapter(homeNewStartRvList, requireContext())
        binding.rvNewStart.adapter = homeNewStartRvAdapter
        binding.rvNewStart.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        homePopularRvAdapter.setOnItemClickListener(object : HomePopularRvAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: ExhibitionsInfo, position: Int) {
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
        })

        getRvData(homePopularRvList)
        getRvData(homeArtisticRvList)
        getRvData(homeEndSoonRvList)
        getRvData(homeNewStartRvList)

        //recyclerView 페이징 처리
//        binding.rvPopular.addOnScrollListener(object : RecyclerView.OnScrollListener(){
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
//                val totalCount = recyclerView.adapter?.itemCount?.minus(2)
//                if(rvPosition == totalCount && hasNextPage){
//                    setExhibitionRvByPage(totalCount)
//                }
//            }
//        })

//        binding.ibFabTop.setOnClickListener {
//            binding.rvHomeExhibition.smoothScrollToPosition(0)
//        }
//        binding.ibFabTopAdmin.setOnClickListener {
//            binding.rvHomeExhibition.smoothScrollToPosition(0)
//        }
//        binding.ivLogo.setOnClickListener {
//            binding.rvPopular.smoothScrollToPosition(0)
//        }

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

    //recyclerView 페이징
//    private fun setExhibitionRvByPage(totalCount:Int){
//        var addItemCount = 0
//        runBlocking(Dispatchers.IO) {
//            val response = ExhibitionRepositoryImpl().getExhibitions(encryptedPrefs.getAT(),exhibitionPage)
//            if(response.isSuccessful && response.body()!!.check ){
//                homePopularRvList.addAll(response.body()!!.information.data)
//                addItemCount = response.body()!!.information.data.size
//                hasNextPage = response.body()!!.information.hasNextPage
//            }
//        }
//        homePopularRvAdapter.notifyItemRangeInserted(totalCount+2, addItemCount)
//        exhibitionPage++
//    }

    private fun getRvData(list: MutableList<ExhibitionsInfo>){
        runBlocking(Dispatchers.IO) {
            val response = ExhibitionRepositoryImpl().getExhibitions(encryptedPrefs.getAT(),0)
            if(response.isSuccessful && response.body()!!.check ){
                list.addAll(response.body()!!.information.data)
            }
        }
    }
}