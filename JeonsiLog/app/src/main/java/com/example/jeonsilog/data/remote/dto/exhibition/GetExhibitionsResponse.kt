package com.example.jeonsilog.data.remote.dto.exhibition

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetExhibitionsResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: ExhibitionData
)
data class ExhibitionData(
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean,
    @SerializedName("data")
    val data: List<ExhibitionsInfo>
)

data class ExhibitionsInfo(
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
    val place: ExhibitionsPlaceEntity
)

data class ExhibitionsPlaceEntity(
    @SerializedName("placeId")
    val placeId: Int?,
    @SerializedName("placeName")
    val placeName: String?,
    @SerializedName("placeAddress")
    val placeAddress: String?
)