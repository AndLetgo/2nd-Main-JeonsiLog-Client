package com.example.jeonsilog.data.remote.dto.report

import com.google.gson.annotations.SerializedName

data class GetReportsResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: ReportsResponseData
)
data class ReportsResponseData(
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean,
    @SerializedName("data")
    val data: List<GetReportsInformation>
)

data class GetReportsInformation(

    @SerializedName("reportId")
    val reportId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("reportType")
    val reportType: String,
    @SerializedName("reportedId")
    val reportedId: Int,
    @SerializedName("clickId")
    val clickId: Int,
    @SerializedName("isChecked")
    val isChecked: Boolean
)
