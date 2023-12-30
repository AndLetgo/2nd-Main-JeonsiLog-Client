package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.data.remote.dto.user.EditNickRequest
import com.example.jeonsilog.data.remote.dto.user.EditNickResponse
import com.example.jeonsilog.data.remote.dto.user.MyInfoResponse
import com.example.jeonsilog.data.remote.dto.user.UnLinkResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface UserApi {
    @GET("/api/users")
    suspend fun getMyInfo(@Header("Authorization") token: String): Response<MyInfoResponse>

    @DELETE("/api/users")
    suspend fun doUnLink(@Header("Authorization") token: String): Response<UnLinkResponse>

    @PATCH("/api/users/nickname")
    suspend fun patchNick(
        @Header("Authorization") token: String,
        @Body requestBody: EditNickRequest
    ): Response<EditNickResponse>
}