package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.reply.PostReportRequest
import com.example.jeonsilog.data.remote.dto.report.GetReportsResponse
import com.example.jeonsilog.data.remote.dto.report.PatchReportRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReportApi {
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

    @PATCH("/api/reports/check")
    suspend fun patchCheckReport(
        @Header("Authorization") token: String,
        @Body body: PatchReportRequest
    ): Response<OnlyMsgResponse>
}