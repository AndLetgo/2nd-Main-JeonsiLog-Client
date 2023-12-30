package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface S3Api {
    @Multipart
    @POST("/s3")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
    ): Response<OnlyMsgResponse>

    @DELETE("/s3")
    suspend fun deleteImage(
        @Header("Authorization") token: String,
        @Query("file") fileName: String
    ): Response<OnlyMsgResponse>

    @GET("/s3/find/{filename}")
    suspend fun getImage(
        @Header("Authorization") token: String,
        @Path("filename") fileName: String
    ): Response<OnlyMsgResponse>
}