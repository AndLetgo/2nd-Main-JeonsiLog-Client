package com.example.jeonsilog.data.remote.dto.follow

import com.google.gson.annotations.SerializedName

data class GetMyFollowingResponse(

    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: GetMyFollowingInformation
)

data class GetMyFollowingInformation(
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean,
    @SerializedName("data")
    val data: List<GetMyFollowingEntity>
)

data class GetMyFollowingEntity(
    @SerializedName("followUserId")
    val followUserId: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImgUrl")
    val profileImgUrl: String
)
