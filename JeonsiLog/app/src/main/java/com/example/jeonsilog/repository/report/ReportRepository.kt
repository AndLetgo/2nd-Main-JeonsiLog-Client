package com.example.jeonsilog.repository.report

import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.reply.PostReportRequest
import com.example.jeonsilog.data.remote.dto.report.GetReportsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReportRepository {
    @GET("/api/reports")
    suspend fun getReports(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): Response<GetReportsResponse>

    @POST("/api/reports")
    suspend fun postReport(
        @Header("Authorization") token: String,
        @Body body: PostReportRequest
    ): Response<OnlyMsgResponse>

    @POST("/api/reports/check/{reportId}")
    suspend fun postCheckReport(
        @Header("Authorization") token: String,
        @Path("reportId") reportId: Int
    ): Response<OnlyMsgResponse>
}