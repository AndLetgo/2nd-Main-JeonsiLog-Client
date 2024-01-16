package com.example.jeonsilog.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class GetReceptionResponse (
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: GetReceptionInformation
)

data class GetReceptionInformation(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("isRecvExhibition")
    val isRecvExhibition: Boolean,
    @SerializedName("isRecvActive")
    val isRecvActive: Boolean
)
