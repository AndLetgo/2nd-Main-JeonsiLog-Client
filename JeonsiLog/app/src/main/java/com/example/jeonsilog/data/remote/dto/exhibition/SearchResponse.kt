package com.example.jeonsilog.data.remote.dto.exhibition

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val informationEntity: List<SearchInformationEntity>
)

data class SearchInformationEntity(
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
    @SerializedName("place")
    val place: SearchPlaceEntity,

)

data class SearchPlaceEntity(
    @SerializedName("placeId")
    val placeId: Int,
    @SerializedName("placeName")
    val placeName: String?,
    @SerializedName("placeAddress")
    val placeAddress: String?
)
