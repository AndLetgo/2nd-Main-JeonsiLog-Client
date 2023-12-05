package com.example.jeonsilog.repository.auth

import com.example.jeonsilog.data.remote.dto.auth.LoginData
import com.example.jeonsilog.data.remote.dto.auth.SignUpData

interface AuthRepository {
    suspend fun postUser(data: SignUpData)

    suspend fun getIsAvailable(nick: String): Boolean

    suspend fun postLogin(data: LoginData)
}