package com.example.jeonsilog.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("check")
    var check: Boolean,
    @SerializedName("information")
    var information: SignUpInformation
)

data class SignUpInformation(
    @SerializedName("message")
    val message: String
)
