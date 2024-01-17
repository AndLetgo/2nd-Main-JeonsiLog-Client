package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.data.remote.dto.exhibition.GetExhibitionResponse
import com.example.jeonsilog.data.remote.dto.exhibition.GetExhibitionsResponse
import com.example.jeonsilog.data.remote.dto.exhibition.GetPosterResponse
import com.example.jeonsilog.data.remote.dto.exhibition.GetRandomPosterResponse
import com.example.jeonsilog.data.remote.dto.exhibition.PatchExhibitionRequest
import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.exhibition.GetCalendarExhibitionResponse
import com.example.jeonsilog.data.remote.dto.exhibition.PatchExhibitionSequenceRequest
import com.example.jeonsilog.data.remote.dto.exhibition.SearchByNameResponse
import com.example.jeonsilog.data.remote.dto.exhibition.SearchResponse
import com.example.jeonsilog.data.remote.dto.review.PatchReviewRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ExhibitionApi {
    @GET("/api/exhibitions")
    suspend fun getExhibitions(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): Response<GetExhibitionsResponse>

    @GET("/api/exhibitions/{id}")
    suspend fun getExhibition(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<GetExhibitionResponse>

    @GET("/api/exhibitions/poster/{id}")
    suspend fun getPoster(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<GetPosterResponse>

    @GET("/api/exhibitions/random")
    suspend fun getRandomPoster(
        @Header("Authorization") token: String
    ): Response<GetRandomPosterResponse>

    @GET("/api/exhibitions/search/{searchWord}")
    suspend fun searchExhibition(
        @Header("Authorization") token: String,
        @Path("searchWord") searchWord: String,
        @Query("page") page: Int
    ): Response<SearchResponse>

    @GET("/api/exhibitions/search/name/{searchWord}")
    suspend fun searchExhibitionByName(
        @Header("Authorization") token: String,
        @Path("searchWord") searchWord: String,
        @Query("page") page: Int
    ): Response<SearchByNameResponse>

    @Multipart
    @PATCH("/api/exhibitions")
    suspend fun patchExhibition(
        @Header("Authorization") token: String,
        @Part("updateExhibitionDetailReq") updateExhibitionDetailReq: RequestBody,
        @Part img: MultipartBody.Part
    ): Response<OnlyMsgResponse>

    @PATCH("/api/exhibitions/sequence")
    suspend fun patchExhibitionSequence(
        @Header("Authorization") token: String,
        @Body body: PatchExhibitionSequenceRequest,
    ): Response<OnlyMsgResponse>

    @GET("/api/exhibitions/search/name/{searchWord}")
    suspend fun searchCalendarExhibition(
        @Header("Authorization") token: String,
        @Path("searchWord") searchWord: String,
        @Query("page") page: Int
    ): Response<GetCalendarExhibitionResponse>

}