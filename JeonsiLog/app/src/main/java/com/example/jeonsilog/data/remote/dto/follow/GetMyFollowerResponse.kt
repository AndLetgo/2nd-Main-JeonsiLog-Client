package com.example.jeonsilog.data.remote.dto.follow

import com.google.gson.annotations.SerializedName

data class GetMyFollowerResponse(

    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: List<GetMyFollowerInformation>
)

data class GetMyFollowerInformation(

    @SerializedName("followUserId")
    val followUserId: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImgUrl")
    val profileImgUrl: String,
    @SerializedName("ifollow")
    val ifollow: Boolean
)