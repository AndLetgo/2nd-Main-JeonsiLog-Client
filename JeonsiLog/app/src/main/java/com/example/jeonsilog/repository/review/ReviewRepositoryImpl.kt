package com.example.jeonsilog.repository.review

import com.example.jeonsilog.data.remote.RetrofitClient
import com.example.jeonsilog.data.remote.api.ReviewApi
import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.review.GetReviewsExhibitionResponse
import com.example.jeonsilog.data.remote.dto.review.GetReviewsResponse
import com.example.jeonsilog.data.remote.dto.review.PostReviewRequest
import retrofit2.Response

class ReviewRepositoryImpl: ReviewRepository {
    private val service = RetrofitClient.getRetrofit()!!.create(ReviewApi::class.java)

    override suspend fun getMyReviews(token: String): Response<GetReviewsResponse> {
        val response = service.getMyReviews("Bearer $token")

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun getOtherReviews(token: String, userId: Int): Response<GetReviewsResponse> {
        val response = service.getOtherReviews("Bearer $token", userId)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun getReviews(
        token: String,
        exhibitionId: Int
    ): Response<GetReviewsExhibitionResponse> {
        val response = service.getReviews("Bearer $token", exhibitionId)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun postReview(
        token: String,
        body: PostReviewRequest
    ): Response<OnlyMsgResponse> {
        val response = service.postReview("Bearer $token", body)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun deleteReview(token: String, reviewId: Int): Response<OnlyMsgResponse> {
        val response = service.deleteReview("Bearer $token", reviewId)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }
}