package com.example.jeonsilog.data.remote.dto.place

import com.google.gson.annotations.SerializedName

data class SearchPlacesResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val informationEntity: List<SearchPlacesInformationEntity>
)

data class SearchPlacesInformationEntity(
    @SerializedName("placeId")
    val placeId: Int,
    @SerializedName("placeName")
    val placeName: String
)
