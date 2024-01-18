package com.example.jeonsilog.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class MyInfoResponse(
    @SerializedName("check")
    var check: Boolean,
    @SerializedName("information")
    var information: MyInfoInformation
)

data class MyInfoInformation(
    @SerializedName("userId")
    var userId: Int,
    @SerializedName("isAdmin")
    val isAdmin: Boolean,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImgUrl")
    val profileImgUrl: String,
    @SerializedName("numFollowing")
    val numFollowing: Int,
    @SerializedName("numFollower")
    val numFollower: Int
)
