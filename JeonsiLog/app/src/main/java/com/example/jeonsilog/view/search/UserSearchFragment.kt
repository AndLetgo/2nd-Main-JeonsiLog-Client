package com.example.jeonsilog.view.search

import android.util.Log
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.UserSearchItem
import com.example.jeonsilog.data.remote.dto.place.SearchPlacesInformationEntity
import com.example.jeonsilog.data.remote.dto.user.SearchUserInformationEntity
import com.example.jeonsilog.databinding.FragmentUserSearchBinding
import com.example.jeonsilog.repository.place.PlaceRepositoryImpl
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.viewmodel.SearchViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking


class UserSearchFragment(private val edittext:String) : BaseFragment<FragmentUserSearchBinding>(R.layout.fragment_user_search) {
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
        //binding.ibFabTopUser.setOnClickListener {
        //    binding.rvUserinfo.scrollToPosition(0)
        //}
        //리사이클러뷰 제어
        binding.rvUserinfo.layoutManager = LinearLayoutManager(requireContext())
        var adapter: UserSearchItemAdapter?
        var list: List<SearchUserInformationEntity>?
        runBlocking(Dispatchers.IO) {
            val response = UserRepositoryImpl().searchUserInfo(GlobalApplication.encryptedPrefs.getAT(),edittext,0)
            if(response.isSuccessful && response.body()!!.check){
                val searchUserInfoResponse = response.body()
                list=searchUserInfoResponse?.informationEntity

            }
            else{
                val searchUserInfoResponse = response.body()
                list=searchUserInfoResponse?.informationEntity

            }
        }
        if (!list.isNullOrEmpty()){
            adapter = context?.let { UserSearchItemAdapter(it,edittext,list!!.toMutableList()) }
            binding.rvUserinfo.adapter = adapter
            checkEmptyListTrue()
        }
    }
}