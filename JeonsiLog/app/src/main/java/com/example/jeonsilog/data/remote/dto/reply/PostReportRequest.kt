package com.example.jeonsilog.data.remote.dto.reply

import com.google.gson.annotations.SerializedName

data class PostReportRequest (

    @SerializedName("reportType")
    val reportType: String,
    @SerializedName("reportedId")
    val reportedId: Int
)