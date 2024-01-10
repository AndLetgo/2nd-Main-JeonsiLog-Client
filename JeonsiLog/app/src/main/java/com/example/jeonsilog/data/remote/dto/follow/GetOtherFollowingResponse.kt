package com.example.jeonsilog.data.remote.dto.follow

import com.google.gson.annotations.SerializedName

data class GetOtherFollowingResponse(

    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: List<GetOtherFollowingInformation>
)

data class GetOtherFollowingInformation(

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
