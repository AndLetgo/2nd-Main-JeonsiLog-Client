package com.example.jeonsilog.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FailureResponse(

    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: FailureInformationEntity
)

data class FailureInformationEntity(
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("errors")
    val errors: List<String>
)