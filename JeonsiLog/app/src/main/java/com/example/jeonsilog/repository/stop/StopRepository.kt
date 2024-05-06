package com.example.jeonsilog.repository.stop

import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.stop.StopReq
import retrofit2.Response

interface StopRepository {
    suspend fun stopUser(token: String, body: StopReq): Response<OnlyMsgResponse>
}