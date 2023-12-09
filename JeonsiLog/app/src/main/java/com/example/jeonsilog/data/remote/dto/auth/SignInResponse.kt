package com.example.jeonsilog.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("check")
    var check: Boolean,
    @SerializedName("information")
    var information: SignInInformation
)

data class SignInInformation(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)
