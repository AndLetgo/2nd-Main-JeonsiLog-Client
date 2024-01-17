package com.example.jeonsilog.repository.report

import com.example.jeonsilog.data.remote.RetrofitClient
import com.example.jeonsilog.data.remote.api.ReportApi
import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.reply.PostReportRequest
import com.example.jeonsilog.data.remote.dto.report.GetReportsResponse
import retrofit2.Response

class ReportRepositoryImpl: ReportRepository {
    private val service = RetrofitClient.getRetrofit()!!.create(ReportApi::class.java)

    override suspend fun getReports(token: String, page: Int): Response<GetReportsResponse> {
        val response = service.getReports(token, page)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun postReport(
        token: String,
        body: PostReportRequest
    ): Response<OnlyMsgResponse> {
        val response = service.postReport(token, body)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun patchCheckReport(token: String, reportId: Int): Response<OnlyMsgResponse> {
        val response = service.patchCheckReport(token, reportId)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

}