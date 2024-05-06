package com.example.jeonsilog.view.admin

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.activityViewModels
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.databinding.FragmentAdminSearchBinding
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.viewmodel.AdminViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.google.android.material.tabs.TabLayoutMediator

class AdminSearchFragment : BaseFragment<FragmentAdminSearchBinding>(R.layout.fragment_admin_search) {
    private val adminViewModel: AdminViewModel by activityViewModels()
    private val tabTextList = listOf("전시", "전시장", "사용자")

    override fun init() {
        GlobalApplication.isRefresh.observe(this){
            if(it){
                (activity as MainActivity).refreshFragment(AdminSearchFragment())
                GlobalApplication.isRefresh.value = false
            }
        }

        binding.lifecycleOwner = this

        binding.vpSearchResult.adapter = AdminSearchVpAdapter(requireActivity())

        TabLayoutMediator(binding.tlSearchResult, binding.vpSearchResult){ tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()

        //키보드 완료 버튼
        binding.etSearchBar.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                adminViewModel.setSearchWord(binding.etSearchBar.text.toString())
            }
            false
        }

        //텍스트 삭제 버튼
        binding.ibResultDelete.setOnClickListener {
            binding.etSearchBar.text.clear()
            adminViewModel.setSearchWord("")
        }
    }

//        adminSearchRvAdapter.setOnItemClickListener(object : AdminSearchExhibitionRvAdapter.OnItemClickListener{
//            override fun onItemClick(v: View, data: SearchInformationEntity, position: Int) {
//                val navController = findNavController()
//                navController.navigate(R.id.adminExhibitionFragment)
//                (activity as MainActivity).setStateFcm(true)
//                isAdminExhibitionOpen =true
//                adminViewModel.setIsReport(true)
//                exhibitionId = data.exhibitionId
//            }
//        })

}