package com.example.jeonsilog.view.admin

import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentAdminSearchPlaceBinding
import com.example.jeonsilog.viewmodel.AdminViewModel
import com.example.jeonsilog.widget.utils.DialogUtil

class AdminSearchPlaceFragment: BaseFragment<FragmentAdminSearchPlaceBinding>(R.layout.fragment_admin_search_place) {
    private val adminViewModel: AdminViewModel by activityViewModels()
    private lateinit var adapter: AdminSearchPlaceRvAdapter
    private var isFinished = true

    override fun init() {
        adapter = AdminSearchPlaceRvAdapter(mutableListOf())
        binding.rvPlace.adapter = adapter
        binding.rvPlace.layoutManager = LinearLayoutManager(requireContext())

        adminViewModel.searchWord.observe(this){
            if(it.isNullOrBlank()){
                Toast.makeText(context,getString(R.string.search_edit_hint), Toast.LENGTH_SHORT).show()
            } else if (adminViewModel.regexPattern.containsMatchIn(it) && it.length <=2){
                Toast.makeText(context, getString(R.string.toast_search_check_regex), Toast.LENGTH_SHORT).show()
            } else {
                adminViewModel.clearList()
                adapter.list.clear()

                adminViewModel.placeHasNextPage = true
                adminViewModel.placeCurrentPage = 0
                adminViewModel.searchPlace()
                DialogUtil().hideSoftKeyboard(requireActivity())
            }
        }

        adminViewModel.placeResultList.observe(this){
            adapter.notifyList(it)

            binding.groupEmpty.visibility = if(adapter.itemCount == 0) View.VISIBLE else View.GONE
        }

        binding.rvPlace.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val rvPos = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = recyclerView.adapter?.itemCount?.minus(1)

                if(totalCount == rvPos){
                    if(isFinished){
                        adminViewModel.searchPlace()
                    }
                }
            }
        })
    }
}