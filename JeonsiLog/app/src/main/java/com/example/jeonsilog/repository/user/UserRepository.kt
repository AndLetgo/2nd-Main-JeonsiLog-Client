package com.example.jeonsilog.repository.user

import com.example.jeonsilog.data.remote.dto.user.EditNickRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface UserRepository {
    @GET("/api/users")
    suspend fun getMyInfo(@Header("Authorization") token: String): Boolean

    @DELETE("/api/users")
    suspend fun doUnLink(@Header("Authorization") token: String): Boolean

    @PATCH("/api/users/nickname")
    suspend fun patchNick(
        @Header("Authorization") token: String,
        @Body requestBody: EditNickRequest
    ): Boolean
}