package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.data.remote.dto.alarm.GetAlarmResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface AlarmApi {
    @GET("/api/alarms/activity")
    suspend fun getActivityAlarm(
        @Header("Authorization") token: String
    ): Response<GetAlarmResponse>

    @GET("/api/alarms/exhibition")
    suspend fun getExhibitionAlarm(
        @Header("Authorization") token: String
    ): Response<GetAlarmResponse>

// 아직 배포 안됨
//    @PATCH("/api/alarms/check/")
//    suspend fun patchAlarmChecked(
//        @Header("Authorization") token: String
//    )
}