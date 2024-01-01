package com.example.jeonsilog.repository.rating

import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.rating.GetRatingsResponse
import com.example.jeonsilog.data.remote.dto.rating.PostRatingRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface RatingRepository {
    @GET("/api/ratings")
    suspend fun getMyRatings(
        @Header("Authorization") token: String
    ): Response<GetRatingsResponse>

    @GET("/api/ratings/{userId}")
    suspend fun getOtherRatings(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Response<GetRatingsResponse>

    @POST("/api/ratings")
    suspend fun postRating(
        @Header("Authorization") token: String,
        @Body body: PostRatingRequest
    ): Response<OnlyMsgResponse>

    @PATCH("/api/ratings")
    suspend fun patchRating(
        @Header("Authorization") token: String,
        @Body body: PostRatingRequest
    ): Response<OnlyMsgResponse>

    @DELETE("/api/ratings/{exhibitionId}")
    suspend fun deleteRating(
        @Header("Authorization") token: String,
        @Path("exhibitionId") exhibitionId: Int
    ): Response<OnlyMsgResponse>
}