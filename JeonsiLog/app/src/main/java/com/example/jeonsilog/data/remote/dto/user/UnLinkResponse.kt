package com.example.jeonsilog.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class UnLinkResponse(
    @SerializedName("check")
    var check: Boolean,
    @SerializedName("information")
    var information: UnLinkInformation
)

data class UnLinkInformation(
    @SerializedName("message")
    val message: String
)
