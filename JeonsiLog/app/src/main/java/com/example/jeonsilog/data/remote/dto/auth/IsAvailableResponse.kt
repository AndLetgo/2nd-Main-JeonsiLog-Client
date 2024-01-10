package com.example.jeonsilog.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class IsAvailableResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: IsAvailableInformation
)

data class IsAvailableInformation(
    @SerializedName("isDuplicate")
    var isDuplicate: Boolean,
    @SerializedName("isForbidden")
    var isForbidden: Boolean
)
