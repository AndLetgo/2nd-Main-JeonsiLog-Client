package com.example.jeonsilog.data.remote.dto.place

import com.google.gson.annotations.SerializedName

data class GetPlacesResponse(

    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val informationEntity: PlacesData
)
data class PlacesData(
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean,
    @SerializedName("data")
    val data: List<GetPlacesInformationEntity>
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
    val imageUrl: String,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("endDate")
    val endDate: String
)
