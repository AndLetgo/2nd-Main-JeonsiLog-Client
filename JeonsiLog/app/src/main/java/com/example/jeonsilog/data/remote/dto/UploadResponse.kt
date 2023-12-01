package com.example.jeonsilog.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<String>
)
