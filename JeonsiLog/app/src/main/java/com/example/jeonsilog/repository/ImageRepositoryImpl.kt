package com.example.jeonsilog.repository

import android.util.Log
import com.example.jeonsilog.data.remote.RetrofitClient
import com.example.jeonsilog.data.remote.api.ImageApi
import okhttp3.MultipartBody

class ImageRepositoryImpl: ImageRepository {

    override suspend fun postProfileImage(image: MultipartBody.Part) {
        val service = RetrofitClient.getRetrofit()!!.create(ImageApi::class.java)
        val response = service.uploadImage(image)

        if(response.isSuccessful){
            response.body()?.let{body->
                Log.d("Upload", body.message)
            }
        }else{
            Log.d("Upload","failure")
        }
    }
}