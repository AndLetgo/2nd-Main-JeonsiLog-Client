package com.example.jeonsilog.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class PatchAlarmFollowingResponse(
    @SerializedName("check")
    var check: Boolean,
    @SerializedName("information")
    var information: PatchAlarmFollowingInformation
)

data class PatchAlarmFollowingInformation(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("isRecvFollowing")
    val isRecvFollowing: Boolean
)
