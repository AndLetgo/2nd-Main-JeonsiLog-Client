package com.example.jeonsilog.data.remote.dto.place


import com.google.gson.annotations.SerializedName

data class SearchPlacesResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: SearchPlacesInformation
)
data class SearchPlacesInformation(
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean,
    @SerializedName("data")
    val data: List<SearchPlacesInformationEntity>
)
data class SearchPlacesInformationEntity(
    @SerializedName("placeId")
    val placeId: Int,
    @SerializedName("placeName")
    val placeName: String,
    @SerializedName("placeAddress")
    val placeAddress: String

)
