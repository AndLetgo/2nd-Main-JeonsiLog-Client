package com.example.jeonsilog.data.remote.dto.review

import com.google.gson.annotations.SerializedName

data class GetReviewsExhibitionResponse (

    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val informationEntity: ReviewsExhibitionData
)
data class ReviewsExhibitionData(
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean,
    @SerializedName("data")
    val data: List<GetReviewsExhibitionInformationEntity>
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
    var contents: String,
    @SerializedName("rate")
    var rate: Double,
    @SerializedName("numReply")
    var numReply: Int,
    @SerializedName("createdDate")
    val createdDate: String
)