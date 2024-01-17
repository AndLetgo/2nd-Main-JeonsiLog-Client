package com.example.jeonsilog.repository.reply

import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.reply.GetReplyResponse
import com.example.jeonsilog.data.remote.dto.reply.PostReplyRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReplyRepository {
    @GET("/api/replies/reviews/{reviewId}")
    suspend fun getReply(
        @Header("Authorization") token: String,
        @Path("reviewId") reviewId: Int,
        @Query("page") page: Int
    ): Response<GetReplyResponse>

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