package com.example.jeonsilog.view.admin

import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.exhibition.SearchInformationEntity
import com.example.jeonsilog.databinding.FragmentAdminSearchExhibitionBinding
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.viewmodel.AdminViewModel
import com.example.jeonsilog.widget.utils.DialogUtil
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.exhibitionId
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isAdminExhibitionOpen

class AdminSearchExhibitionFragment: BaseFragment<FragmentAdminSearchExhibitionBinding>(R.layout.fragment_admin_search_exhibition) {
    private val adminViewModel: AdminViewModel by activityViewModels()
    private lateinit var adapter: AdminSearchExhibitionRvAdapter
    private var isFinished = true

    override fun init() {
        adapter = AdminSearchExhibitionRvAdapter(mutableListOf(), requireContext())
        binding.rvExhibition.adapter = adapter
        binding.rvExhibition.layoutManager = LinearLayoutManager(requireContext())

        adminViewModel.searchWord.observe(this){
            if(it.isNullOrBlank()){
                Toast.makeText(context,getString(R.string.search_edit_hint), Toast.LENGTH_SHORT).show()
            } else if (adminViewModel.regexPattern.containsMatchIn(it) && it.length <=2){
                Toast.makeText(context, getString(R.string.toast_search_check_regex), Toast.LENGTH_SHORT).show()
            } else {
                adminViewModel.clearList()
                adapter.list.clear()

                adminViewModel.exhibitionHasNextPage = true
                adminViewModel.exhibitionCurrentPage = 0
                adminViewModel.searchExhibition()
                DialogUtil().hideSoftKeyboard(requireActivity())
            }
        }

        adminViewModel.exhibitionResultList.observe(this){
            adapter.notifyList(it)

            binding.groupEmpty.visibility = if(adapter.itemCount == 0) View.VISIBLE else View.GONE
        }

        binding.rvExhibition.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val rvPos = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = recyclerView.adapter?.itemCount?.minus(1)

                if(totalCount == rvPos){
                    if(isFinished){
                        adminViewModel.searchExhibition()
                    }
                }
            }
        })

        adapter.setOnItemClickListener(object : AdminSearchExhibitionRvAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: SearchInformationEntity, position: Int) {
                val navController = findNavController()
                navController.navigate(R.id.adminExhibitionFragment)
                (activity as MainActivity).setStateFcm(true)
                isAdminExhibitionOpen =true
                adminViewModel.setIsReport(false)
                exhibitionId = data.exhibitionId
            }
        })
    }
}