package com.example.jeonsilog.repository.place

import com.example.jeonsilog.data.remote.dto.place.GetPlacesResponse
import com.example.jeonsilog.data.remote.dto.place.SearchPlacesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceRepository {
    @GET("/api/places/exhibition/{placeId}")
    suspend fun getPlaces(
        @Header("Authorization") token: String,
        @Path("placeId") placeId: Int,
        @Query("page") page: Int
    ): Response<GetPlacesResponse>

    @GET("/api/places/search/{searchWord}")
    suspend fun searchPlaces(
        @Header("Authorization") token: String,
        @Path("searchWord") searchWord: String,
        @Query("page") page: Int
    ): Response<SearchPlacesResponse>
}