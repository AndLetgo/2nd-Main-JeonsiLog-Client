package com.example.jeonsilog.repository.exhibition

import com.example.jeonsilog.data.remote.RetrofitClient
import com.example.jeonsilog.data.remote.api.ExhibitionApi
import com.example.jeonsilog.data.remote.dto.exhibition.GetExhibitionResponse
import com.example.jeonsilog.data.remote.dto.exhibition.GetExhibitionsResponse
import com.example.jeonsilog.data.remote.dto.exhibition.GetPosterResponse
import com.example.jeonsilog.data.remote.dto.exhibition.GetRandomPosterResponse
import com.example.jeonsilog.data.remote.dto.exhibition.PatchExhibitionRequest
import com.example.jeonsilog.data.remote.dto.exhibition.PatchExhibitionResponse
import com.example.jeonsilog.data.remote.dto.exhibition.SearchResponse
import retrofit2.Response

class ExhibitionRepositoryImpl: ExhibitionRepository {
    private val service = RetrofitClient.getRetrofit()!!.create(ExhibitionApi::class.java)

    override suspend fun getExhibitions(
        token: String,
        page: Int
    ): Response<GetExhibitionsResponse> {
        val response = service.getExhibitions("Bearer $token", page)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun getExhibition(token: String, id: Int): Response<GetExhibitionResponse> {
        val response = service.getExhibition("Bearer $token", id)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun getPoster(token: String, id: Int): Response<GetPosterResponse> {
        val response = service.getPoster("Bearer $token", id)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun getRandomPoster(token: String): Response<GetRandomPosterResponse> {
        val response = service.getRandomPoster("Bearer $token")

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun searchExhibition(
        token: String,
        searchWord: String,
        page: Int
    ): Response<SearchResponse> {
        val response = service.searchExhibition("Bearer $token", searchWord, page)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun patchExhibition(
        token: String,
        body: PatchExhibitionRequest
    ): Response<PatchExhibitionResponse> {
        val response = service.patchExhibition("Bearer $token", body)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }
}