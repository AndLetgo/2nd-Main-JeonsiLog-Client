package com.example.jeonsilog.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class EditNickResponse(
    @SerializedName("check")
    var check: Boolean,
    @SerializedName("information")
    var information: EditNickInformation
)

data class EditNickInformation(
    @SerializedName("message")
    val message: String
)