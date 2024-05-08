package com.example.jeonsilog.data.remote.dto.calendar

import com.google.gson.annotations.SerializedName

data class GetPhotoResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: List<GetPhotoInformation>
)

data class GetPhotoInformation(
    @SerializedName("calendarId")
    val calendarId: Int,
    @SerializedName("date")
    val date: String,
    @SerializedName("imgUrl")
    val imgUrl: String,
    @SerializedName("caption")
    val caption: String
)
