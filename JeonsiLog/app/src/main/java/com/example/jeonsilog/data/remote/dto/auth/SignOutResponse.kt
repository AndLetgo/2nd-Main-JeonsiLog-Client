package com.example.jeonsilog.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class SignOutResponse(
    @SerializedName("check")
    var check: Boolean,
    @SerializedName("information")
    var information: SignOutInformation
)

data class SignOutInformation(
    @SerializedName("message")
    val message: String
)
