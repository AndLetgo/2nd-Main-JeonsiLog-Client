package com.example.jeonsilog.data.remote.dto.interest

import com.google.gson.annotations.SerializedName

data class PostInterestResponse(

    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: PostInterestInformationEntity
)

data class PostInterestInformationEntity(
    @SerializedName("interestId")
    val interestId: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("exhibitionId")
    val exhibitionId: Int
)
