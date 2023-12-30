package com.example.jeonsilog.repository.place

import com.example.jeonsilog.data.remote.RetrofitClient
import com.example.jeonsilog.data.remote.api.PlaceApi
import com.example.jeonsilog.data.remote.dto.place.GetPlacesResponse
import com.example.jeonsilog.data.remote.dto.place.SearchPlacesResponse
import retrofit2.Response

class PlaceRepositoryImpl: PlaceRepository {
    private val service = RetrofitClient.getRetrofit()!!.create(PlaceApi::class.java)

    override suspend fun getPlaces(
        token: String,
        placeId: Int,
        page: Int
    ): Response<GetPlacesResponse> {
        val response = service.getPlaces("Bearer $token", placeId, page)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun searchPlaces(
        token: String,
        searchWord: String,
        page: Int
    ): Response<SearchPlacesResponse> {
        val response = service.searchPlaces("Bearer $token", searchWord, page)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }
}