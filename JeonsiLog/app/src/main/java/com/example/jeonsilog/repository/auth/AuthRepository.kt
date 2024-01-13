package com.example.jeonsilog.repository.auth

import com.example.jeonsilog.data.remote.dto.auth.IsAvailableResponse
import com.example.jeonsilog.data.remote.dto.auth.SignInRequest
import com.example.jeonsilog.data.remote.dto.auth.SignUpRequest
import retrofit2.Response


interface AuthRepository {
    suspend fun postSignUp(data: SignUpRequest): Boolean

    suspend fun getIsAvailable(nick: String): Response<IsAvailableResponse>

    suspend fun signIn(data: SignInRequest): Boolean

    suspend fun signOut(token: String): Boolean

    suspend fun tokenRefresh(token: String): Boolean
}