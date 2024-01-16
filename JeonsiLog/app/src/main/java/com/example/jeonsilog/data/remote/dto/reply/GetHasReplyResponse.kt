package com.example.jeonsilog.data.remote.dto.reply

import com.google.gson.annotations.SerializedName

data class GetHasReplyResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: HasReplyEntity
)

data class HasReplyEntity(
    @SerializedName("replyId")
    val replyId: Int,
    @SerializedName("isExist")
    val isExist: Boolean
)