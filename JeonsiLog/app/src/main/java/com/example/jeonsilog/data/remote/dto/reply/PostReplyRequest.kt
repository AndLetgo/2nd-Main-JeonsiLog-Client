package com.example.jeonsilog.data.remote.dto.reply

import com.google.gson.annotations.SerializedName

data class PostReplyRequest(
    @SerializedName("reviewId")
    val reviewId: Int,
    @SerializedName("contents")
    val contents: String
)
