package com.example.jeonsilog.repository.reply

import com.example.jeonsilog.data.remote.RetrofitClient
import com.example.jeonsilog.data.remote.api.ReplyApi
import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.reply.GetReplyResponse
import com.example.jeonsilog.data.remote.dto.reply.PostReplyRequest
import retrofit2.Response

class ReplyRepositoryImpl: ReplyRepository {
    private val service = RetrofitClient.getRetrofit()!!.create(ReplyApi::class.java)

    override suspend fun getReply(
        token: String,
        reviewId: Int,
        page: Int
    ): Response<GetReplyResponse> {
        val response = service.getReply(token, reviewId, page)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun postReply(
        token: String,
        body: PostReplyRequest
    ): Response<OnlyMsgResponse> {
        val response = service.postReply(token, body)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun deleteReply(token: String, replyId: Int): Response<OnlyMsgResponse> {
        val response = service.deleteReply(token, replyId)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }
}