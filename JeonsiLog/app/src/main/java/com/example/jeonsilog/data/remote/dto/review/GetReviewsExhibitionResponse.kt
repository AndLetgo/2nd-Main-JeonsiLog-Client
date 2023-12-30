package com.example.jeonsilog.data.remote.dto.review

import com.google.gson.annotations.SerializedName

data class GetReviewsExhibitionResponse (

    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val informationEntity: List<GetReviewsExhibitionInformationEntity>
)

data class GetReviewsExhibitionInformationEntity(
    @SerializedName("reviewId")
    val reviewId: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("imgUrl")
    val imgUrl: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("contents")
    val contents: String,
    @SerializedName("rate")
    val rate: Double,
    @SerializedName("numReply")
    val numReply: Int
)