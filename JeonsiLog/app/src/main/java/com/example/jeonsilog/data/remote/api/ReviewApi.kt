package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.review.GetCheckReviewResponse
import com.example.jeonsilog.data.remote.dto.review.GetReviewResponse
import com.example.jeonsilog.data.remote.dto.review.GetReviewsExhibitionInformationEntity
import com.example.jeonsilog.data.remote.dto.review.GetReviewsExhibitionResponse
import com.example.jeonsilog.data.remote.dto.review.GetReviewsResponse
import com.example.jeonsilog.data.remote.dto.review.PatchReviewRequest
import com.example.jeonsilog.data.remote.dto.review.PostReviewRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewApi {
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

    @GET("/api/reviews/check/{exhibitionId}")
    suspend fun getCheckHasReview(
        @Header("Authorization") token: String,
        @Path("exhibitionId") exhibitionId: Int
    ): Response<GetCheckReviewResponse>

    @POST("/api/reviews")
    suspend fun postReview(
        @Header("Authorization") token: String,
        @Body body: PostReviewRequest
    ): Response<OnlyMsgResponse>

    @PATCH("/api/reviews")
    suspend fun patchReview(
        @Header("Authorization") token: String,
        @Body body: PatchReviewRequest
    ):Response<OnlyMsgResponse>

    @DELETE("/api/reviews/{reviewId}")
    suspend fun deleteReview(
        @Header("Authorization") token: String,
        @Path("reviewId") reviewId: Int
    ): Response<OnlyMsgResponse>
}