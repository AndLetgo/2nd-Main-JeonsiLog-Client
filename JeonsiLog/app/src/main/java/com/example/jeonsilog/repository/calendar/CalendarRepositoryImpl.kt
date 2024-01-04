package com.example.jeonsilog.repository.calendar

import com.example.jeonsilog.data.remote.RetrofitClient
import com.example.jeonsilog.data.remote.api.CalendarApi
import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.calendar.DeletePhotoRequest
import com.example.jeonsilog.data.remote.dto.calendar.GetPhotoResponse
import com.example.jeonsilog.data.remote.dto.calendar.PostPhotoFromGalleryRequest
import com.example.jeonsilog.data.remote.dto.calendar.PostPhotoFromPosterRequest

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class CalendarRepositoryImpl: CalendarRepository {
    private val service = RetrofitClient.getRetrofit()!!.create(CalendarApi::class.java)

    override suspend fun getMyPhotoMonth(token: String, date: String): Response<GetPhotoResponse> {
        val response = service.getMyPhotoMonth("Bearer $token", date)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun getOtherPhotoMonth(
        token: String,
        userId: Int,
        date: String
    ): Response<GetPhotoResponse> {
        val response = service.getOtherPhotoMonth("Bearer $token", userId, date)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun postPhotoFromPoster(
        token: String,
        body: PostPhotoFromPosterRequest
    ): Response<OnlyMsgResponse> {
        val response = service.postPhotoFromPoster("Bearer $token", body)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun postPhotoFromGallery(
        token: String,
        uploadImageReq: RequestBody,
        img: MultipartBody.Part
    ): Response<OnlyMsgResponse> {
        val response = service.postPhotoFromGallery("Bearer $token", uploadImageReq, img)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun deletePhoto(
        token: String,
        body: DeletePhotoRequest
    ): Response<OnlyMsgResponse> {
        val response = service.deletePhoto("Bearer $token", body)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

}