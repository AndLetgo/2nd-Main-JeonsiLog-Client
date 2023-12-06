package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.data.remote.dto.auth.IsAvailableResponse
import com.example.jeonsilog.data.remote.dto.auth.LoginData
import com.example.jeonsilog.data.remote.dto.auth.TokenResponse
import com.example.jeonsilog.data.remote.dto.auth.ReissuedData
import com.example.jeonsilog.data.remote.dto.auth.SignUpData
import com.example.jeonsilog.data.remote.dto.auth.OnlyMsgResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {
    @POST("/auth/sign-up")
    suspend fun postSignUp(@Body userData: SignUpData): Response<OnlyMsgResponse>

    @GET("/auth/nickname/{nick}")
    suspend fun getIsAvailable(@Path("nick") nick: String): Response<IsAvailableResponse>

    @POST("/auth/sign-in")
    suspend fun postLogin(@Body loginData: LoginData): Response<TokenResponse>

    @POST("/auth/refresh")
    suspend fun postReissue(@Body reissueData: ReissuedData): Response<TokenResponse>
//
//    @POST("/auth/sign-out")
//    suspend fun postLogOut(@Body)
}