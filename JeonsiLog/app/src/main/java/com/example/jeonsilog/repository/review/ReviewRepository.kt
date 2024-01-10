package com.example.jeonsilog.repository.review

import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.review.GetReviewResponse
import com.example.jeonsilog.data.remote.dto.review.GetReviewsExhibitionResponse
import com.example.jeonsilog.data.remote.dto.review.GetReviewsResponse
import com.example.jeonsilog.data.remote.dto.review.PostReviewRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewRepository {
    @GET("/api/reviews")
    suspend fun getMyReviews(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): Response<GetReviewsResponse>

    @GET("/api/reviews/{userId}")
    suspend fun getOtherReviews(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Query("page") page: Int
    ): Response<GetReviewsResponse>

    @GET("api/reviews/exhibition/{exhibitionId}")
    suspend fun getReviews(
        @Header("Authorization") token: String,
        @Path("exhibitionId") exhibitionId: Int,
        @Query("page") page: Int
    ): Response<GetReviewsExhibitionResponse>

    @GET("api/reviews/review/{reviewId}")
    suspend fun getReview(
        @Header("Authorization") token: String,
        @Path("reviewId") reviewId: Int
    ): Response<GetReviewResponse>

    @POST("/api/reviews")
    suspend fun postReview(
        @Header("Authorization") token: String,
        @Body body: PostReviewRequest
    ): Response<OnlyMsgResponse>

    @DELETE("/api/reviews/{reviewId}")
    suspend fun deleteReview(
        @Header("Authorization") token: String,
        @Path("reviewId") reviewId: Int
    ): Response<OnlyMsgResponse>
}