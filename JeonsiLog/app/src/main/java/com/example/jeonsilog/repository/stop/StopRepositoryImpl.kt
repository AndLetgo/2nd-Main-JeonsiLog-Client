package com.example.jeonsilog.repository.stop

import com.example.jeonsilog.data.remote.RetrofitClient
import com.example.jeonsilog.data.remote.api.StopApi
import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.stop.StopReq
import retrofit2.Response


class StopRepositoryImpl: StopRepository {
    private val service = RetrofitClient.getRetrofit()!!.create(StopApi::class.java)
    private val tag = this.javaClass.simpleName

    override suspend fun stopUser(token: String, body: StopReq): Response<OnlyMsgResponse> {
        val response = service.stopUser(token, body)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }
}