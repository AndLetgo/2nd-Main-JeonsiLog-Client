package com.example.jeonsilog.view.admin

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.adapters.ViewBindingAdapter.OnViewAttachedToWindow
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.exhibition.ExhibitionsInfo
import com.example.jeonsilog.data.remote.dto.exhibition.SearchInformationEntity
import com.example.jeonsilog.data.remote.dto.exhibition.SearchPlaceEntity
import com.example.jeonsilog.databinding.FragmentAdminSearchBinding
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.view.home.HomeRvAdapter
import com.example.jeonsilog.viewmodel.AdminViewModel
import com.example.jeonsilog.widget.utils.DialogUtil
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.exhibitionId
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isAdminExhibitionOpen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class AdminSearchFragment : BaseFragment<FragmentAdminSearchBinding>(R.layout.fragment_admin_search) {
    private val regexPattern = Regex("[!@#\\\$%^&*(),.?\\\":{}|<>;]")
    private val adminViewModel: AdminViewModel by activityViewModels()
    private lateinit var adminSearchRvAdapter: AdminSearchRvAdapter
    private var searchList = mutableListOf<SearchInformationEntity>()

    private var searchPage = 0
    private var hasNextPage = true
    private var searchWord = ""
    val TAG = "search"

    override fun init() {
        GlobalApplication.isRefresh.observe(this){
            if(it){
                (activity as MainActivity).refreshFragment(AdminSearchFragment())
                GlobalApplication.isRefresh.value = false
            }
        }

        binding.lifecycleOwner = this
        searchList = mutableListOf<SearchInformationEntity>()
        adminSearchRvAdapter = AdminSearchRvAdapter(searchList, requireContext())
        binding.rvSearchResult.adapter = adminSearchRvAdapter
        binding.rvSearchResult.layoutManager = LinearLayoutManager(requireContext())

        adminSearchRvAdapter.setOnItemClickListener(object : AdminSearchRvAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: SearchInformationEntity, position: Int) {
                Log.d(TAG, "onItemClick: ")
                val navController = findNavController()
                navController.navigate(R.id.adminExhibitionFragment)
                (activity as MainActivity).setStateFcm(true)
                isAdminExhibitionOpen =true
                adminViewModel.setIsReport(true)
                exhibitionId = data.exhibitionId
            }
        })

        //recyclerView 페이징 처리
        binding.rvSearchResult.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = recyclerView.adapter?.itemCount?.minus(1)
                if(rvPosition == totalCount && hasNextPage){
                    setSearchResultRvByPage(totalCount)
                }
            }
        })

        //키보드 완료 버튼 눌럿을시 실행
        binding.etSearchBar.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                // 키보드의 완료 버튼이 눌렸을 때 수행할 동작
                binding.ivEmptySearchBefore.visibility = View.GONE
                letsSearch()
            }
            false
        }

        //텍스트 삭제 버튼
        binding.ibResultDelete.setOnClickListener {
            binding.etSearchBar.text.clear()
            searchWord = ""
            binding.ivEmptySearchBefore.visibility = View.VISIBLE
//            binding.ivEmptySearchNotResult.visibility = View.GONE
        }
    }

    //검색
    private fun letsSearch(){
        searchWord = binding.etSearchBar.text.toString()
        if(searchWord.isBlank()){
            Toast.makeText(context,getString(R.string.search_edit_hint), Toast.LENGTH_SHORT).show()
        }else if(regexPattern.containsMatchIn(searchWord)&&searchWord.length<=2){
            Toast.makeText(context, getString(R.string.toast_search_check_regex), Toast.LENGTH_SHORT).show()
        }else{
            searchList = mutableListOf()
            searchPage = 0
            hasNextPage = true
            setSearchResultRvByPage(0)
            DialogUtil().hideSoftKeyboard(requireActivity())
        }
    }

    //recyclerView 페이징
    private fun setSearchResultRvByPage(totalCount:Int){
        if(searchWord.isNullOrEmpty()){
            return
        }
        var addItemCount = 0
        runBlocking(Dispatchers.IO){
            val response = ExhibitionRepositoryImpl().searchExhibition(encryptedPrefs.getAT(),searchWord,searchPage)
            if(response.isSuccessful && response.body()!!.check){
                adminSearchRvAdapter.exhibitionList.addAll(response.body()!!.information.data)
//                searchList.addAll(response.body()!!.information.data)
                addItemCount = response.body()!!.information.data.size
                hasNextPage = response.body()!!.information.hasNextPage
            }
        }
        Log.d(TAG, "setSearchResultRvByPage: searchList.size: ${searchList.size}")
        if(searchList.size == 0){
//            binding.ivEmptySearchNotResult.visibility = View.VISIBLE
        }else{
            Log.d(TAG, "setSearchResultRvByPage: visible")
//            binding.ivEmptySearchBefore.visibility = View.GONE
//            binding.ivEmptySearchNotResult.visibility = View.GONE
//            binding.rvSearchResult.visibility = View.VISIBLE
        }
        val startPosition = totalCount
//        adminSearchRvAdapter = AdminSearchRvAdapter(searchList, requireContext())
//        adminSearchRvAdapter.notifyItemRangeInserted(startPosition, addItemCount)
        adminSearchRvAdapter.notifyDataSetChanged()
        searchPage++
    }
}