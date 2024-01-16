package com.example.jeonsilog.data.remote.dto.review

import com.google.gson.annotations.SerializedName

data class GetCheckReviewResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: CheckReviewEntity
)

data class CheckReviewEntity(
    @SerializedName("isWrite")
    val isWrite:Boolean,
    @SerializedName("reviewId")
    val reviewId:Int,
    @SerializedName("contents")
    val contents:String,
)
