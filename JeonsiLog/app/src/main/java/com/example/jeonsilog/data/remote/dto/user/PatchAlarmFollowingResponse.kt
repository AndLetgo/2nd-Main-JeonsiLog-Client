package com.example.jeonsilog.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class PatchAlarmExhibitionResponse(
    @SerializedName("check")
    var check: Boolean,
    @SerializedName("information")
    var information: PatchAlarmExhibitionInformation
)

data class PatchAlarmExhibitionInformation(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("isRecvFollowing")
    val isRecvFollowing: Boolean
)
