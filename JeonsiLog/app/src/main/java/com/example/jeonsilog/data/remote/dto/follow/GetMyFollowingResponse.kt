package com.example.jeonsilog.data.remote.dto.follow

import com.google.gson.annotations.SerializedName

data class GetMyFollowingResponse(

    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: List<GetMyFollowingInformation>
)

data class GetMyFollowingInformation(
    @SerializedName("followUserId")
    val followUserId: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImgUrl")
    val profileImgUrl: String
)
