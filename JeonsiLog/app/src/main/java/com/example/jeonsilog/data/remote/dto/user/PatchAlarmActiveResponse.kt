package com.example.jeonsilog.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class PatchAlarmActiveResponse(
    @SerializedName("check")
    var check: Boolean,
    @SerializedName("information")
    var information: PatchAlarmActiveInformation
)

data class PatchAlarmActiveInformation(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("isRecvActive")
    val isRecvActive: Boolean
)