package com.example.jeonsilog.data.remote.dto.exhibition

import com.example.jeonsilog.data.remote.dto.follow.GetMyFollowerEntity
import com.google.gson.annotations.SerializedName

data class GetCalendarExhibitionResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: CalendarExhibitionInformation
)
data class CalendarExhibitionInformation(
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean,
    @SerializedName("data")
    val data: List<GetCalendarExhibitionInformation>
)
data class GetCalendarExhibitionInformation(
    @SerializedName("exhibitionId")
    val exhibitionId: Int,
    @SerializedName("exhibitionName")
    val exhibitionName: String,
    @SerializedName("imageUrl")
    val imageUrl: String
)

