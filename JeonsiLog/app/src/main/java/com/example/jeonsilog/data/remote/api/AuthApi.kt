package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.data.remote.dto.auth.IsAvailableResponse
import com.example.jeonsilog.data.remote.dto.auth.SignInRequest
import com.example.jeonsilog.data.remote.dto.auth.SignInResponse
import com.example.jeonsilog.data.remote.dto.auth.SignOutResponse
import com.example.jeonsilog.data.remote.dto.auth.SignUpRequest
import com.example.jeonsilog.data.remote.dto.auth.SignUpResponse
import com.example.jeonsilog.data.remote.dto.auth.TokenRefreshRequest
import com.example.jeonsilog.data.remote.dto.auth.TokenRefreshResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {
    @POST("/auth/sign-up")
    suspend fun postSignUp(@Body userData: SignUpRequest): Response<SignUpResponse>

    @GET("/auth/nickname/{nick}")
    suspend fun getIsAvailable(@Path("nick") nick: String): Response<IsAvailableResponse>

    @POST("/auth/sign-in")
    suspend fun signIn(@Body loginData: SignInRequest): Response<SignInResponse>

    @POST("/auth/refresh")
    suspend fun tokenRefresh(@Body token: TokenRefreshRequest): Response<TokenRefreshResponse>

    @POST("/auth/sign-out")
    suspend fun signOut(@Header("Authorization") token: String): Response<SignOutResponse>
}