package com.example.jeonsilog.data.remote.dto.rating

import com.google.gson.annotations.SerializedName

data class PostRatingRequest(

    @SerializedName("exhibitionId")
    val exhibitionId: Int,
    @SerializedName("rate")
    val rate: Double
)
