package com.example.jeonsilog.data.remote.dto.place

import com.google.gson.annotations.SerializedName

data class GetPlacesResponse(

    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val informationEntity: List<GetPlacesInformationEntity>
)

data class GetPlacesInformationEntity(
    @SerializedName("exhibitionId")
    val exhibitionId: Int,
    @SerializedName("exhibitionName")
    val exhibitionName: String,
    @SerializedName("operatingKeyword")
    val operatingKeyword: String,
    @SerializedName("priceKeyword")
    val priceKeyword: String,
    @SerializedName("imageUrl")
    val imageUrl: String
)
