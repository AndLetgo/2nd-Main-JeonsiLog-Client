package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.follow.GetMyFollowingResponse
import com.example.jeonsilog.data.remote.dto.follow.GetOtherFollowerResponse
import com.example.jeonsilog.data.remote.dto.follow.GetOtherFollowingResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface FollowApi {
    @GET("/api/follows/follower")
    suspend fun getMyFollower(
        @Header("Authorization") token: String
    ): Response<GetOtherFollowingResponse>

    @GET("/api/follows/follower/{userId}")
    suspend fun getOtherFollower(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Response<GetOtherFollowerResponse>

    @GET("/api/follows/following")
    suspend fun getMyFollowing(
        @Header("Authorization") token: String
    ): Response<GetMyFollowingResponse>

    @GET("/api/follows/following/{userId}")
    suspend fun getOtherFollowing(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Response<GetOtherFollowingResponse>

    @POST("/api/follows/{userId}")
    suspend fun postFollow(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Response<OnlyMsgResponse>

    @DELETE("/api/follows/{userId}")
    suspend fun deleteFollow(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Response<OnlyMsgResponse>

    @DELETE("/api/follows/follower/{userId}")
    suspend fun deleteFollower(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Response<OnlyMsgResponse>
}