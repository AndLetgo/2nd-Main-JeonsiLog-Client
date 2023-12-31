package com.example.jeonsilog.repository.user

import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.user.EditNickRequest
import com.example.jeonsilog.data.remote.dto.user.MyInfoResponse
import com.example.jeonsilog.data.remote.dto.user.PatchAlarmActiveResponse
import com.example.jeonsilog.data.remote.dto.user.PatchAlarmFollowingResponse
import com.example.jeonsilog.data.remote.dto.user.PatchCalendarOpenResponse
import com.example.jeonsilog.data.remote.dto.user.SearchUserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Path

interface UserRepository {
    @GET("/api/users")
    suspend fun getMyInfo(@Header("Authorization") token: String): Boolean

    @DELETE("/api/users")
    suspend fun doUnLink(@Header("Authorization") token: String): Boolean

    @PATCH("/api/users/nickname")
    suspend fun patchNick(
        @Header("Authorization") token: String,
        @Body requestBody: EditNickRequest
    ): Boolean

    @GET("/api/users/{userId}")
    suspend fun getOtherInfo(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Response<MyInfoResponse>

    @GET("/api/users/search/{searchWord}")
    suspend fun searchUserInfo(
        @Header("Authorization") token: String,
        @Path("searchWord") searchWord: String
    ): Response<SearchUserResponse>

    @PATCH("/api/users/calendar")
    suspend fun patchCalendarOpen(
        @Header("Authorization") token: String,
    ): Response<PatchCalendarOpenResponse>

    @PATCH("/api/users/alarm-active")
    suspend fun patchAlarmActive(
        @Header("Authorization") token: String,
    ): Response<PatchAlarmActiveResponse>

    @PATCH("/api/users/alarm-following")
    suspend fun patchAlarmFollowing(
        @Header("Authorization") token: String,
    ): Response<PatchAlarmFollowingResponse>

    @Multipart
    @PATCH("/api/users/profile")
    suspend fun uploadProfileImg(
        @Header("Authorization") token: String,
        @Part img: MultipartBody.Part,
    ): Response<OnlyMsgResponse>
}