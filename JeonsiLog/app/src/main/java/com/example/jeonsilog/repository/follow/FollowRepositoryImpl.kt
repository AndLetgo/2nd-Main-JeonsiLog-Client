package com.example.jeonsilog.repository.follow

import com.example.jeonsilog.data.remote.RetrofitClient
import com.example.jeonsilog.data.remote.api.FollowApi
import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.follow.GetMyFollowingResponse
import com.example.jeonsilog.data.remote.dto.follow.GetOtherFollowerResponse
import com.example.jeonsilog.data.remote.dto.follow.GetOtherFollowingResponse
import retrofit2.Response

class FollowRepositoryImpl: FollowRepository {
    private val service = RetrofitClient.getRetrofit()!!.create(FollowApi::class.java)

    override suspend fun getMyFollower(token: String): Response<GetOtherFollowingResponse> {
        val response = service.getMyFollower("Bearer $token")

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun getOtherFollower(token: String, userId: Int): Response<GetOtherFollowerResponse> {
        val response = service.getOtherFollower("Bearer $token", userId)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun getMyFollowing(token: String): Response<GetMyFollowingResponse> {
        val response = service.getMyFollowing("Bearer $token")

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun getOtherFollowing(token: String, userId: Int): Response<GetOtherFollowingResponse> {
        val response = service.getOtherFollowing("Bearer $token", userId)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun postFollow(token: String, userId: Int): Response<OnlyMsgResponse> {
        val response = service.postFollow("Bearer $token", userId)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun deleteFollow(token: String, userId: Int): Response<OnlyMsgResponse> {
        val response = service.deleteFollow("Bearer $token", userId)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun deleteFollower(token: String, userId: Int): Response<OnlyMsgResponse> {
        val response = service.deleteFollower("Bearer $token", userId)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

}