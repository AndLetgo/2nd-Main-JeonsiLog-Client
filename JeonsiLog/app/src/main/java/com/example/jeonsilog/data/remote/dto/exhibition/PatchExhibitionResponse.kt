package com.example.jeonsilog.data.remote.dto.exhibition

import com.google.gson.annotations.SerializedName

data class PatchExhibitionResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val informationEntity: PatchExhibitionInformationEntity
)

data class PatchExhibitionInformationEntity(
    @SerializedName("message")
    val message: String
)
