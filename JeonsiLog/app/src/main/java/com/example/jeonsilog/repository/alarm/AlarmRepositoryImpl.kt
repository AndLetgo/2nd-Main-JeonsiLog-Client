package com.example.jeonsilog.repository.alarm

import com.example.jeonsilog.data.remote.RetrofitClient
import com.example.jeonsilog.data.remote.api.AlarmApi
import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.alarm.GetAlarmResponse
import retrofit2.Response

class AlarmRepositoryImpl: AlarmRepository {
    private val service = RetrofitClient.getRetrofit()!!.create(AlarmApi::class.java)

    override suspend fun getActivityAlarm(token: String, page: Int): Response<GetAlarmResponse> {
        val response = service.getActivityAlarm("Bearer $token", page)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun getExhibitionAlarm(token: String, page: Int): Response<GetAlarmResponse> {
        val response = service.getExhibitionAlarm("Bearer $token", page)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun patchAlarmChecked(token: String, alarmId: Int): Response<OnlyMsgResponse> {
        val response = service.patchAlarmChecked("Bearer $token", alarmId)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }
}