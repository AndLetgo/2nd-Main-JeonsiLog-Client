package com.example.jeonsilog.data.remote.dto.exhibition

import com.google.gson.annotations.SerializedName

data class SearchByNameResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: SearchByNameInformation
)

data class SearchByNameInformation(
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean,
    @SerializedName("data")
    val data: List<SearchByNameEntity>
)

data class SearchByNameEntity(
    @SerializedName("exhibitionId")
    val exhibitionId: Int,
    @SerializedName("exhibitionName")
    val exhibitionName: String
)