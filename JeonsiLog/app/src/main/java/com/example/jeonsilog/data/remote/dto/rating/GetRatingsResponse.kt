package com.example.jeonsilog.data.remote.dto.rating

import com.google.gson.annotations.SerializedName

data class GetRatingsResponse(

    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: GetMyRatingsInformationEntity
)

data class GetMyRatingsInformationEntity(
    @SerializedName("numRating")
    val numRating: Int,
    @SerializedName("data")
    val dataEntity: List<GetMyRatingsDataEntity>
)

data class GetMyRatingsDataEntity(
    @SerializedName("ratingId")
    val ratingId: Int,
    @SerializedName("exhibitionId")
    val exhibitionId: Int,
    @SerializedName("exhibitionName")
    val exhibitionName: String,
    @SerializedName("rate")
    val rate: Double
)
