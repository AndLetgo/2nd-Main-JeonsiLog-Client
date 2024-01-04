package com.example.jeonsilog.data.remote.dto.reply

import com.google.gson.annotations.SerializedName

data class GetReplyResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: List<GetReplyInformation>
)

data class GetReplyInformation(

    @SerializedName("replyId")
    val replyId: Int,
    @SerializedName("contents")
    val contents: String,
    @SerializedName("createdDate")
    val createdDate: String,
    @SerializedName("user")
    val user: UserEntity
)

data class UserEntity(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImgUrl")
    val profileImgUrl: String
)
