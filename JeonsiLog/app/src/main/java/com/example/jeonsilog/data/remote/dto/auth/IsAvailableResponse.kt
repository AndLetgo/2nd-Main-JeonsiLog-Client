package com.example.jeonsilog.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class IsAvailableResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: InformationEntity
)

data class InformationEntity(
    @SerializedName("isAvailable")
    val isAvailable: Boolean
)
