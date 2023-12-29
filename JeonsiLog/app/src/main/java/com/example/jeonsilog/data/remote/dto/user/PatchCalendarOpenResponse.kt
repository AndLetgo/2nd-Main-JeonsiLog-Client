package com.example.jeonsilog.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class PatchCalendarOpenResponse(
    @SerializedName("check")
    var check: Boolean,
    @SerializedName("information")
    var information: PatchCalendarOpenInformation
)

data class PatchCalendarOpenInformation(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("open")
    val open: Boolean
)