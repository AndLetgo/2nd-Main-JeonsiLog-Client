package com.example.jeonsilog.data.remote.dto.exhibition

import com.google.gson.annotations.SerializedName

data class GetCalendarExhibitionResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: List<GetCalendarExhibitionInformation>
)
data class GetCalendarExhibitionInformation(
    @SerializedName("exhibitionId")
    val exhibitionId: Int,
    @SerializedName("exhibitionName")
    val exhibitionName: String,
    @SerializedName("imageUrl")
    val imageUrl: String
)

