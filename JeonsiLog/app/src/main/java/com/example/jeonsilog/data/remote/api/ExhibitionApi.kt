package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.data.remote.dto.exhibition.GetExhibitionResponse
import com.example.jeonsilog.data.remote.dto.exhibition.GetExhibitionsResponse
import com.example.jeonsilog.data.remote.dto.exhibition.GetPosterResponse
import com.example.jeonsilog.data.remote.dto.exhibition.GetRandomPosterResponse
import com.example.jeonsilog.data.remote.dto.exhibition.PatchExhibitionRequest
import com.example.jeonsilog.data.remote.dto.exhibition.PatchExhibitionResponse
import com.example.jeonsilog.data.remote.dto.exhibition.SearchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface ExhibitionApi {
    @GET("/exhibitions")
    suspend fun getExhibitions(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): Response<GetExhibitionsResponse>

    @GET("/exhibitions/{id}")
    suspend fun getExhibition(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<GetExhibitionResponse>

    @GET("/exhibitions/poster/{id}")
    suspend fun getPoster(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<GetPosterResponse>

    @GET("/exhibitions/random")
    suspend fun getRandomPoster(
        @Header("Authorization") token: String
    ): Response<GetRandomPosterResponse>

    @GET("/exhibitions/search/{searchWord}")
    suspend fun searchExhibition(
        @Header("Authorization") token: String,
        @Path("searchWord") searchWord: String,
        @Query("page") page: Int
    ): Response<SearchResponse>

    @PATCH("/exhibitions")
    suspend fun patchExhibition(
        @Header("Authorization") token: String,
        @Body body: PatchExhibitionRequest
    ): Response<PatchExhibitionResponse>
}