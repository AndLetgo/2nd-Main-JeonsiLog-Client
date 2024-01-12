package com.example.jeonsilog.data.remote.dto.review

import com.google.gson.annotations.SerializedName

data class GetReviewsResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: GetReviewsInformation
)

data class GetReviewsInformation(
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean,
    @SerializedName("numReview")
    val numReview: Int,
    @SerializedName("data")
    val data: List<GetReviewsEntity>
)

data class GetReviewsEntity(
    @SerializedName("reviewId")
    val reviewId: Int,
    @SerializedName("exhibitionId")
    val exhibitionId: Int,
    @SerializedName("exhibitionName")
    val exhibitionName: String,
    @SerializedName("exhibitionImgUrl")
    val exhibitionImgUrl: String,
    @SerializedName("contents")
    val contents: String
)
