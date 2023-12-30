package com.example.jeonsilog.data.remote.dto.review

import com.google.gson.annotations.SerializedName

data class PostReviewRequest(

    @SerializedName("exhibitionId")
    val exhibitionId: Int,
    @SerializedName("contents")
    val contents: String
)
