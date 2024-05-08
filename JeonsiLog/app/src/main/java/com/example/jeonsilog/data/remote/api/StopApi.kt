package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.stop.StopReq
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface StopApi {
    @POST("/api/stop")
    suspend fun stopUser(
        @Header("Authorization") token: String,
        @Body body: StopReq
    ): Response<OnlyMsgResponse>
}