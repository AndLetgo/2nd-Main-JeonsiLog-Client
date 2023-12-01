package com.example.jeonsilog.repository

import okhttp3.MultipartBody

interface ImageRepository {
    suspend fun postProfileImage(image: MultipartBody.Part)
}