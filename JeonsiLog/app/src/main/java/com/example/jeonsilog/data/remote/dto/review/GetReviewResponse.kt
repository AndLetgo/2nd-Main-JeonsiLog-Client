package com.example.jeonsilog.data.remote.dto.review

import com.google.gson.annotations.SerializedName

data class GetReviewResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: GetReviewsExhibitionInformationEntity
)
