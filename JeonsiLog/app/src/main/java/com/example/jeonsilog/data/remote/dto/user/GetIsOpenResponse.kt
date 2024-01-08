package com.example.jeonsilog.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class GetIsOpenResponse(
    @SerializedName("check")
    var check: Boolean,
    @SerializedName("information")
    var information: GetIsOpenInformation
)

data class GetIsOpenInformation(
    @SerializedName("isOpen")
    var isOpen: Boolean
)
