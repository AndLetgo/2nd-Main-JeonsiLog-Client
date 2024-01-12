package com.example.jeonsilog.data.remote.dto.user

import com.example.jeonsilog.data.remote.dto.exhibition.SearchInformation
import com.example.jeonsilog.data.remote.dto.exhibition.SearchInformationEntity
import com.google.gson.annotations.SerializedName

data class SearchUserResponse(

    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: SearchUserInformation
)
data class SearchUserInformation(
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean,
    @SerializedName("data")
    val data: List<SearchUserInformationEntity>
)
data class SearchUserInformationEntity(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImgUrl")
    val profileImgUrl: String
)