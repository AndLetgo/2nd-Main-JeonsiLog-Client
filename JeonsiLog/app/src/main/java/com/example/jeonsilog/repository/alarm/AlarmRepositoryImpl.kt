package com.example.jeonsilog.repository.alarm

import com.example.jeonsilog.data.remote.RetrofitClient
import com.example.jeonsilog.data.remote.api.AlarmApi
import com.example.jeonsilog.data.remote.dto.alarm.GetAlarmResponse
import retrofit2.Response

class AlarmRepositoryImpl: AlarmRepository {
    private val service = RetrofitClient.getRetrofit()!!.create(AlarmApi::class.java)

    override suspend fun getActivityAlarm(token: String): Response<GetAlarmResponse> {
        val response = service.getActivityAlarm("Bearer $token")

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun getExhibitionAlarm(token: String): Response<GetAlarmResponse> {
        val response = service.getExhibitionAlarm("Bearer $token")

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    // 아직 배포 안됨
    //    override suspend fun patchAlarmChecked(token: String)
}