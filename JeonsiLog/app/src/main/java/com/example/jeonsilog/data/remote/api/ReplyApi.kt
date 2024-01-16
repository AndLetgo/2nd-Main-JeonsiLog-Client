package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.reply.GetHasReplyResponse
import com.example.jeonsilog.data.remote.dto.reply.GetReplyResponse
import com.example.jeonsilog.data.remote.dto.reply.PostReplyRequest
import com.example.jeonsilog.data.remote.dto.review.GetReviewResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReplyApi {

    @GET("/api/replies/reviews/{reviewId}")
    suspend fun getReply(
        @Header("Authorization") token: String,
        @Path("reviewId") reviewId: Int,
        @Query("page") page: Int
    ): Response<GetReplyResponse>

    @GET("/api/replies/{replyId}")
    suspend fun getHasReply(
        @Header("Authorization") token: String,
        @Path("replyId") replyId: Int
    ): Response<GetHasReplyResponse>

    @POST("/api/replies")
    suspend fun postReply(
        @Header("Authorization") token: String,
        @Body body: PostReplyRequest
    ): Response<OnlyMsgResponse>

    @DELETE("/api/replies/{replyId}")
    suspend fun deleteReply(
        @Header("Authorization") token: String,
        @Path("replyId") replyId: Int
    ): Response<OnlyMsgResponse>
}