package com.example.jeonsilog.data.remote.dto.alarm

import com.google.gson.annotations.SerializedName

data class GetAlarmResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: List<AlarmInformation>
)

data class AlarmInformation(
    @SerializedName("alarmId")
    var alarmId: Int,
    @SerializedName("alarmType")
    val alarmType: String,
    @SerializedName("targetId")
    var targetId: Int,
    @SerializedName("clickId")
    var clickId: Int,
    @SerializedName("imgUrl")
    val imgUrl: String,
    @SerializedName("contents")
    val contents: String,
    @SerializedName("dateTime")
    val dateTime: String,
    @SerializedName("isChecked")
    val isChecked: Boolean

)
