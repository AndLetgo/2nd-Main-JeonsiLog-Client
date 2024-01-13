package com.example.jeonsilog.data.remote.dto.follow

import com.google.gson.annotations.SerializedName

data class GetOtherFollowerResponse(

    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: GetOtherFollowerInformation
)

data class GetOtherFollowerInformation(
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean,
    @SerializedName("data")
    val data: List<GetOtherFollowerEntity>
)

data class GetOtherFollowerEntity(

    @SerializedName("followUserId")
    val followUserId: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImgUrl")
    val profileImgUrl: String,
    @SerializedName("followMe")
    val followMe: Boolean,
    @SerializedName("ifollow")
    val ifollow: Boolean
)