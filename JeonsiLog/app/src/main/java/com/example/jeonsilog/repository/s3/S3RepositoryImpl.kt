package com.example.jeonsilog.repository.s3

import com.example.jeonsilog.data.remote.RetrofitClient
import com.example.jeonsilog.data.remote.api.S3Api
import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import okhttp3.MultipartBody
import retrofit2.Response

class S3RepositoryImpl: S3Repository {
    private val service = RetrofitClient.getRetrofit()!!.create(S3Api::class.java)

    override suspend fun uploadImage(
        token: String,
        file: MultipartBody.Part,
    ): Response<OnlyMsgResponse> {
        val response = service.uploadImage("Bearer $token", file)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun deleteImage(token: String, fileName: String): Response<OnlyMsgResponse> {
        val response = service.deleteImage("Bearer $token", fileName)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun getImage(token: String, fileName: String): Response<OnlyMsgResponse> {
        val response = service.getImage("Bearer $token", fileName)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

}