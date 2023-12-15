package com.example.jeonsilog.repository.user

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header

interface UserRepository {
    @GET("/api/users")
    suspend fun getMyInfo(@Header("Authorization") token: String): Boolean

    @DELETE("/api/users")
    suspend fun doUnLink(@Header("Authorization") token: String): Boolean
}