package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.interest.GetInterestResponse
import com.example.jeonsilog.data.remote.dto.interest.PostInterestResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface InterestApi {
    @GET("/api/interest")
    suspend fun getInterest(
        @Header("Authorization") token: String
    ): Response<GetInterestResponse>

    @POST("/api/interest/{exhibitionId}")
    suspend fun postInterest(
        @Header("Authorization") token: String,
        @Path("exhibitionId") exhibitionId: Int
    ): Response<PostInterestResponse>

    @DELETE("/api/interest/{exhibitionId}")
    suspend fun deleteInterest(
        @Header("Authorization") token: String,
        @Path("exhibitionId") exhibitionId: Int
    ): Response<OnlyMsgResponse>
}