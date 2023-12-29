package com.example.jeonsilog.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class SearchUserResponse(

    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val informationEntity: List<SearchUserInformationEntity>
)

data class SearchUserInformationEntity(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImgUrl")
    val profileImgUrl: String
)