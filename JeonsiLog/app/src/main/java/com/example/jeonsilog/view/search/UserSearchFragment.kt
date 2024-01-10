package com.example.jeonsilog.view.search

import android.util.Log
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.UserSearchItem
import com.example.jeonsilog.data.remote.dto.exhibition.SearchInformationEntity
import com.example.jeonsilog.data.remote.dto.place.SearchPlacesInformationEntity
import com.example.jeonsilog.data.remote.dto.user.SearchUserInformationEntity
import com.example.jeonsilog.databinding.FragmentUserSearchBinding
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.repository.place.PlaceRepositoryImpl
import com.example.jeonsilog.repository.user.UserRepositoryImpl
import com.example.jeonsilog.viewmodel.SearchViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking


class UserSearchFragment(private val edittext:String) : BaseFragment<FragmentUserSearchBinding>(R.layout.fragment_user_search) {

    private lateinit var userItemAdapter: UserSearchItemAdapter
    val UserRvList= mutableListOf<SearchUserInformationEntity>()
    var itemPage=0
    var hasNextPage=true



    lateinit var viewModel: SearchViewModel
    override fun init() {
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        userItemAdapter = UserSearchItemAdapter(requireContext(),UserRvList)
        binding.rvUserinfo.adapter = userItemAdapter
        binding.rvUserinfo.layoutManager = LinearLayoutManager(requireContext())

        setUserRvByPage(0)
        binding.rvUserinfo.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = recyclerView.adapter?.itemCount?.minus(2)
                if(rvPosition == totalCount && hasNextPage){
                    setUserRvByPage(totalCount)
                }
            }
        })
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
    private fun setUserRvByPage(totalCount:Int){
        var addItemCount = 0
        runBlocking(Dispatchers.IO) {
            val response = UserRepositoryImpl().searchUserInfo(encryptedPrefs.getAT(),edittext,itemPage)
            if(response.isSuccessful && response.body()!!.check){
                UserRvList.addAll(response.body()!!.information.data.toMutableList())
                addItemCount = response.body()!!.information.data.size
                hasNextPage = response.body()!!.information.hasNextPage
                checkEmptyListTrue()
            }else{
                checkEmptyListFalse()
            }
        }
        val startPosition = totalCount + 1
        userItemAdapter.notifyItemRangeInserted(startPosition,addItemCount)
        itemPage++
    }

}