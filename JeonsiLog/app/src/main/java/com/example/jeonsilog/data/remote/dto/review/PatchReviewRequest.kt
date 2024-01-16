package com.example.jeonsilog.data.remote.dto.review

import com.google.gson.annotations.SerializedName

data class PatchReviewRequest(
    @SerializedName("reviewId")
    val reviewId: Int,
    @SerializedName("contents")
    val contents: String
)
