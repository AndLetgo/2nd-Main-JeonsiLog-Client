package com.example.jeonsilog.repository.alarm

import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.alarm.GetAlarmResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface AlarmRepository {
    @GET("/api/alarms/activity")
    suspend fun getActivityAlarm(
        @Header("Authorization") token: String
    ): Response<GetAlarmResponse>

    @GET("/api/alarms/exhibition")
    suspend fun getExhibitionAlarm(
        @Header("Authorization") token: String
    ): Response<GetAlarmResponse>

    @PATCH("/api/alarms/check/{alarmId}")
    suspend fun patchAlarmChecked(
        @Header("Authorization") token: String,
        @Path("alarmId") alarmId: Int
    ): Response<OnlyMsgResponse>
}