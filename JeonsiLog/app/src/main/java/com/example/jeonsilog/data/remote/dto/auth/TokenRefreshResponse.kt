package com.example.jeonsilog.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class TokenRefreshResponse(
    @SerializedName("check")
    var check: Boolean,
    @SerializedName("information")
    var information: TokenRefreshInformation
)

data class TokenRefreshInformation(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)
