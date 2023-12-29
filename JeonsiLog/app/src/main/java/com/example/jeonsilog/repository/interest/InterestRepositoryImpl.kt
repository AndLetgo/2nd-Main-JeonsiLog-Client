package com.example.jeonsilog.repository.interest

import com.example.jeonsilog.data.remote.RetrofitClient
import com.example.jeonsilog.data.remote.api.InterestApi
import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.interest.GetInterestResponse
import com.example.jeonsilog.data.remote.dto.interest.PostInterestResponse
import retrofit2.Response

class InterestRepositoryImpl: InterestRepository {
    private val service = RetrofitClient.getRetrofit()!!.create(InterestApi::class.java)

    override suspend fun getInterest(token: String): Response<GetInterestResponse> {
        val response = service.getInterest("Bearer $token")

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun postInterest(token: String, exhibitionId: Int): Response<PostInterestResponse> {
        val response = service.postInterest("Bearer $token", exhibitionId)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun deleteInterest(
        token: String,
        exhibitionId: Int
    ): Response<OnlyMsgResponse> {
        val response = service.deleteInterest("Bearer $token", exhibitionId)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

}