package com.example.jeonsilog.repository.calendar

import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.calendar.GetPhotoResponse
import com.example.jeonsilog.data.remote.dto.calendar.PostPhotoFromGalleryRequest
import com.example.jeonsilog.data.remote.dto.calendar.PostPhotoFromPosterRequest
import com.example.jeonsilog.data.remote.dto.calendar.UploadImageReqEntity
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface CalendarRepository {
    @GET("/api/calendars/{date}")
    suspend fun getMyPhotoMonth(
        @Header("Authorization") token: String,
        @Path("date") date: String
    ): Response<GetPhotoResponse>

    @GET("/api/calendars/{userId}/{date}")
    suspend fun getOtherPhotoMonth(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Path("date") date: String
    ): Response<GetPhotoResponse>

    @POST("/api/calendars/exhibition")
    suspend fun postPhotoFromPoster(
        @Header("Authorization") token: String,
        @Body body: PostPhotoFromPosterRequest
    ): Response<OnlyMsgResponse>

    @Multipart
    @POST("/api/calendars/image")
    suspend fun postPhotoFromGallery(
        @Header("Authorization") token: String,
        @Part("uploadImageReq") uploadImageReq: PostPhotoFromGalleryRequest,
        @Part img: MultipartBody.Part,
    ): Response<OnlyMsgResponse>

    @DELETE("/api/calendars")
    suspend fun deletePhoto(
        @Header("Authorization") token: String,
        @Body body: UploadImageReqEntity
    ): Response<OnlyMsgResponse>
}