package com.example.jeonsilog.data.remote.dto.report

import com.google.gson.annotations.SerializedName

data class PatchReportRequest(
    @SerializedName("reportType")
    val reportType: String,
    @SerializedName("reportedId")
    val reportedId: Int
)
