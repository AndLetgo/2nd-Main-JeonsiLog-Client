package com.example.jeonsilog.repository.auth

import com.example.jeonsilog.data.remote.dto.auth.LoginData
import com.example.jeonsilog.data.remote.dto.auth.SignUpData

interface AuthRepository {
    suspend fun postSignUp(data: SignUpData): Boolean

    suspend fun getIsAvailable(nick: String): Boolean

    suspend fun postLogin(data: LoginData)
}