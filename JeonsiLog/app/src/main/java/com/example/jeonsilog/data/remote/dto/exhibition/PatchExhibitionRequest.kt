package com.example.jeonsilog.data.remote.dto.exhibition

import com.example.jeonsilog.data.remote.dto.calendar.UploadImageReqEntity
import com.google.gson.annotations.SerializedName

//data class PatchExhibitionRequest (
//
//    @SerializedName("updateExhibitionDetailReq")
//    val updateExhibitionDetailReq: UpdateExhibitionInfoEntity,
//    @SerializedName("img")
//    val img: String,
//)
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
    val information: String?,
    @SerializedName("isImageChange")
    val isImageChange: Boolean,
    @SerializedName("updatePlaceInfo")
    val updatePlaceInfo: UpdatePlaceInfoEntity
)

data class UpdatePlaceInfoEntity(
    @SerializedName("placeId")
    val placeId: Int?,
    @SerializedName("placeName")
    val placeName: String?,
    @SerializedName("placeAddress")
    val placeAddress: String?,
    @SerializedName("placeTel")
    val placeTel: String?,
    @SerializedName("placeHomePage")
    val placeHomePage: String?
)
