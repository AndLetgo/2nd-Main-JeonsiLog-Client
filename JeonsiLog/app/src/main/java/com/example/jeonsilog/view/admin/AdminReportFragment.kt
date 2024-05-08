package com.example.jeonsilog.view.admin

import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jeonsilog.R
import com.example.jeonsilog.base.BaseFragment
import com.example.jeonsilog.data.remote.dto.report.GetReportsInformation
import com.example.jeonsilog.data.remote.dto.report.PatchReportRequest
import com.example.jeonsilog.databinding.FragmentAdminReportBinding
import com.example.jeonsilog.repository.report.ReportRepositoryImpl
import com.example.jeonsilog.view.MainActivity
import com.example.jeonsilog.viewmodel.AdminViewModel
import com.example.jeonsilog.widget.utils.GlobalApplication
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.exhibitionId
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.isAdminExhibitionOpen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class AdminReportFragment : BaseFragment<FragmentAdminReportBinding>(R.layout.fragment_admin_report) {
    private val adminViewModel: AdminViewModel by activityViewModels()
    private lateinit var adminReportRvAdapter : AdminReportRvAdapter
    private lateinit var reportList: MutableList<GetReportsInformation>
    private var reportPage = 0
    private var hasNextPage = true

    override fun init() {
        (activity as MainActivity).setStateToolBar(false)

        GlobalApplication.isRefresh.observe(this){
            if(it){
                (activity as MainActivity).refreshFragment(AdminReportFragment())
                GlobalApplication.isRefresh.value = false
            }
        }
        if(isAdminExhibitionOpen){
            (activity as MainActivity).refreshFragment(AdminReportFragment())
            isAdminExhibitionOpen =false
        }

        reportList = mutableListOf()

        adminReportRvAdapter = AdminReportRvAdapter(reportList, requireContext())
        binding.rvReport.adapter = adminReportRvAdapter
        binding.rvReport.layoutManager = LinearLayoutManager(this.context)
        adminReportRvAdapter.setOnItemClickListener(object : AdminReportRvAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: GetReportsInformation, position: Int) {
                val navController = findNavController()
                when(data.reportType){
                    "EXHIBITION" -> {
                        adminViewModel.setReportExhibitionId(data.clickId)
                        adminViewModel.setIsReport(true)
                        navController.navigate(R.id.adminExhibitionFragment)
                    }
                    "REVIEW" -> {
                        adminViewModel.setReportReviewId(data.clickId)
                        navController.navigate(R.id.adminReviewFragment)
                    }
                    "REPLY" -> {
                        adminViewModel.setReportReviewId(data.clickId)
                        navController.navigate(R.id.adminReviewFragment)
                    }
                    else -> {
                        adminViewModel.setReportExhibitionId(data.clickId)
                        Log.d("report", "onItemClick: clickId: ${data.clickId}")
                        exhibitionId = data.clickId
                        navController.navigate(R.id.adminExhibitionFragment)
                    }
                }
                checkReport(v, data.reportType,data.reportedId)
                isAdminExhibitionOpen = true
                (activity as MainActivity).setStateFcm(true)
            }
        })

        setReportRvByPage(0)
        //recyclerView 페이징 처리
        binding.rvReport.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val rvPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val totalCount = recyclerView.adapter?.itemCount?.minus(2)
                if(rvPosition == totalCount && hasNextPage){
                    setReportRvByPage(totalCount)
                }
            }
        })
    }

    //recyclerView 페이징
    private fun setReportRvByPage(totalCount:Int){
        var addItemCount = 0
        runBlocking(Dispatchers.IO) {
            val response = ReportRepositoryImpl().getReports(encryptedPrefs.getAT(),reportPage)
            if(response.isSuccessful && response.body()!!.check ){
                reportList.addAll(response.body()!!.information.data)
                addItemCount = response.body()!!.information.data.size
                hasNextPage = response.body()!!.information.hasNextPage
            }
        }
        val startPosition = totalCount + 1
        adminReportRvAdapter.notifyItemRangeInserted(startPosition, addItemCount)
        reportPage++
    }

    //신고 확인
    private fun checkReport(view:View, reportType:String, reportedId:Int){
        val body = PatchReportRequest(reportType, reportedId)
        runBlocking(Dispatchers.IO){
            ReportRepositoryImpl().patchCheckReport(encryptedPrefs.getAT(), body)
        }
        view.alpha = 0.4f
    }
}