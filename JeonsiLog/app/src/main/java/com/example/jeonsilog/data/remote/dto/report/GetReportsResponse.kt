package com.example.jeonsilog.data.remote.dto.report

import com.google.gson.annotations.SerializedName

data class GetReportsResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: GetReportsInformation
)

data class GetReportsInformation(

    @SerializedName("reportId")
    val reportId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("reportType")
    val reportType: String,
    @SerializedName("reportedId")
    val reportedId: Int
)
