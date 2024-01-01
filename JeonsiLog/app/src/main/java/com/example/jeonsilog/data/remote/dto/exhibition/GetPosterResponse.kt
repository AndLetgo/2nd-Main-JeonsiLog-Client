package com.example.jeonsilog.data.remote.dto.exhibition

import com.google.gson.annotations.SerializedName

data class GetPosterResponse(

    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: PosterInformationEntity
)

data class PosterInformationEntity(
    @SerializedName("exhibitionId")
    val exhibitionId: Int,
    @SerializedName("imageUrl")
    val imageUrl: String
)
