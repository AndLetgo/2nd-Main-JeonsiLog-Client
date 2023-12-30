package com.example.jeonsilog.data.remote.dto.exhibition

import com.google.gson.annotations.SerializedName

data class PatchExhibitionRequest(

    @SerializedName("exhibitionId")
    val exhibitionId: Int,
    @SerializedName("exhibitionName")
    val exhibitionName: String,
    @SerializedName("operatingKeyword")
    val operatingKeyword: String,
    @SerializedName("priceKeyword")
    val priceKeyword: String,
    @SerializedName("information")
    val information: String,
    @SerializedName("updatePlaceInfo")
    val updatePlaceInfo: UpdatePlaceInfoEntity
)

data class UpdatePlaceInfoEntity(
    @SerializedName("placeId")
    val placeId: Int,
    @SerializedName("placeAddress")
    val placeAddress: String,
    @SerializedName("operatingTime")
    val operatingTime: String,
    @SerializedName("closedDays")
    val closedDays: String,
    @SerializedName("placeTel")
    val placeTel: String,
    @SerializedName("placeHomePage")
    val placeHomePage: String
)
