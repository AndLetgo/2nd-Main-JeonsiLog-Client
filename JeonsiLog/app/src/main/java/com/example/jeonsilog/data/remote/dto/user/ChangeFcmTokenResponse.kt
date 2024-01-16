package com.example.jeonsilog.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class ChangeFcmTokenResponse(
    @SerializedName("check")
    var check: Boolean,
    @SerializedName("information")
    var information: ChangeFcmTokenInformation
)

data class ChangeFcmTokenInformation(
    @SerializedName("message")
    val message: String
)


