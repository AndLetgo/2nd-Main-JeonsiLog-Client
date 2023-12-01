package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.data.remote.dto.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageApi {
    @Multipart
    @POST("upload")
    fun uploadImage(@Part image: MultipartBody.Part): Response<UploadResponse>
}