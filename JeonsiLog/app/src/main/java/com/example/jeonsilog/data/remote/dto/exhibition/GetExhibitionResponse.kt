package com.example.jeonsilog.data.remote.dto.exhibition

import com.google.gson.annotations.SerializedName

data class GetExhibitionResponse(

    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: ExhibitionInfo
)

data class ExhibitionInfo(
    @SerializedName("exhibitionId")
    val exhibitionId: Int,
    @SerializedName("exhibitionName")
    val exhibitionName: String,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("operatingKeyword")
    val operatingKeyword: String,
    @SerializedName("priceKeyword")
    val priceKeyword: String,
    @SerializedName("information")
    val information: String,
    @SerializedName("rate")
    val rate: Float,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("checkInterest")
    val checkInterest: Boolean,
    @SerializedName("myRating")
    val myRating: Float,
    @SerializedName("place")
    val place: ExhibitionPlaceEntity
)

data class ExhibitionPlaceEntity(
    @SerializedName("placeId")
    val placeId: Int,
    @SerializedName("placeName")
    val placeName: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("tel")
    val tel: String,
    @SerializedName("homePage")
    val homePage: String
)
