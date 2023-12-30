package com.example.jeonsilog.data.remote.dto.interest

import com.google.gson.annotations.SerializedName

data class GetInterestResponse(

    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val informationEntity: List<GetInterestInformationEntity>
)

data class GetInterestInformationEntity(
    @SerializedName("interestId")
    val interestId: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("exhibitionId")
    val exhibitionId: Int,
    @SerializedName("exhibitionName")
    val exhibitionName: String,
    @SerializedName("placeName")
    val placeName: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("operatingKeyword")
    val operatingKeyword: String,
    @SerializedName("priceKeyword")
    val priceKeyword: String
)
