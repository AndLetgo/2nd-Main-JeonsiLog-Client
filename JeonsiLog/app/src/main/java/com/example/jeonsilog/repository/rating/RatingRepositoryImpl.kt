package com.example.jeonsilog.repository.rating

import com.example.jeonsilog.data.remote.RetrofitClient
import com.example.jeonsilog.data.remote.api.RatingApi
import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.rating.GetRatingsResponse
import com.example.jeonsilog.data.remote.dto.rating.PostRatingRequest
import retrofit2.Response

class RatingRepositoryImpl: RatingRepository {
    private val service = RetrofitClient.getRetrofit()!!.create(RatingApi::class.java)

    override suspend fun getMyRatings(token: String): Response<GetRatingsResponse> {
        val response = service.getMyRatings("Bearer $token")

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun getOtherRatings(token: String, userId: Int): Response<GetRatingsResponse> {
        val response = service.getOtherRatings("Bearer $token", userId)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun postRating(
        token: String,
        body: PostRatingRequest
    ): Response<OnlyMsgResponse> {
        val response = service.postRating("Bearer $token", body)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun patchRating(
        token: String,
        body: PostRatingRequest
    ): Response<OnlyMsgResponse> {
        val response = service.patchRating("Bearer $token", body)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun deleteRating(token: String, exhibitionId: Int): Response<OnlyMsgResponse> {
        val response = service.deleteRating("Bearer $token", exhibitionId)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }
}