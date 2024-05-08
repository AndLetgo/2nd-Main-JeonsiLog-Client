package com.example.jeonsilog.view.admin

import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentAdminSearchUserBinding
import com.example.jeonsilog.viewmodel.AdminViewModel
import com.example.jeonsilog.widget.utils.DialogUtil

class AdminSearchUserFragment: BaseFragment<FragmentAdminSearchUserBinding>(R.layout.fragment_admin_search_user) {
    private val adminViewModel: AdminViewModel by activityViewModels()
    private lateinit var adapter: AdminSearchUserRvAdapter
    private var isFinished = true

    override fun init() {
        adapter = AdminSearchUserRvAdapter(mutableListOf(), requireContext())
        binding.rvUser.adapter = adapter
        binding.rvUser.layoutManager = LinearLayoutManager(requireContext())

        adminViewModel.searchWord.observe(this){
            if(it.isNullOrBlank()){
                Toast.makeText(context,getString(R.string.search_edit_hint), Toast.LENGTH_SHORT).show()
            } else if (adminViewModel.regexPattern.containsMatchIn(it) && it.length <=2){
                Toast.makeText(context, getString(R.string.toast_search_check_regex), Toast.LENGTH_SHORT).show()
            } else {
                adminViewModel.clearList()
                adapter.list.clear()

                adminViewModel.userHasNextPage = true
                adminViewModel.userCurrentPage = 0
                adminViewModel.searchUser()
                DialogUtil().hideSoftKeyboard(requireActivity())
            }
        }

        adminViewModel.userResultList.observe(this){
            adapter.notifyList(it)

            binding.groupEmpty.visibility = if(adapter.itemCount == 0) View.VISIBLE else View.GONE
        }

        binding.rvUser.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val rvPos = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = recyclerView.adapter?.itemCount?.minus(1)

                if(totalCount == rvPos){
                    if(isFinished){
                        adminViewModel.searchUser()
                    }
                }
            }
        })
    }
}