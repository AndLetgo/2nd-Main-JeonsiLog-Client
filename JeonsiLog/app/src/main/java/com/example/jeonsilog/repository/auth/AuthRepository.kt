package com.example.jeonsilog.repository.auth

import com.example.jeonsilog.data.remote.dto.auth.SignInRequest
import com.example.jeonsilog.data.remote.dto.auth.SignUpRequest


interface AuthRepository {
    suspend fun postSignUp(data: SignUpRequest): Boolean

    suspend fun getIsAvailable(nick: String): Boolean

    suspend fun signIn(data: SignInRequest): Boolean

    suspend fun signOut(token: String): Boolean

    suspend fun tokenRefresh(token: String): Boolean
}