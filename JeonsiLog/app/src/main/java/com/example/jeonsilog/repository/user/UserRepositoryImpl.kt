package com.example.jeonsilog.repository.user

import android.util.Log
import com.example.jeonsilog.data.remote.RetrofitClient
import com.example.jeonsilog.data.remote.api.UserApi
import com.example.jeonsilog.data.remote.dto.OnlyMsgResponse
import com.example.jeonsilog.data.remote.dto.user.ChangeFcmTokenRequest
import com.example.jeonsilog.data.remote.dto.user.EditNickRequest
import com.example.jeonsilog.data.remote.dto.user.GetIsOpenResponse
import com.example.jeonsilog.data.remote.dto.user.GetReceptionResponse
import com.example.jeonsilog.data.remote.dto.user.MyInfoResponse
import com.example.jeonsilog.data.remote.dto.user.PatchAlarmActiveResponse
import com.example.jeonsilog.data.remote.dto.user.PatchAlarmExhibitionResponse
import com.example.jeonsilog.data.remote.dto.user.PatchCalendarOpenResponse
import com.example.jeonsilog.data.remote.dto.user.SearchUserResponse
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.prefs
import okhttp3.MultipartBody
import retrofit2.Response


class UserRepositoryImpl: UserRepository {
    private val service = RetrofitClient.getRetrofit()!!.create(UserApi::class.java)
    private val tag = this.javaClass.simpleName

    override suspend fun getMyInfo(token: String): Boolean {
        val response = service.getMyInfo("Bearer $token")

        return if(response.isSuccessful && response.body()?.check == true){
            encryptedPrefs.setNN(response.body()!!.information.nickname)
            encryptedPrefs.setCheckAdmin(response.body()!!.information.isAdmin)
            encryptedPrefs.setUI(response.body()!!.information.userId)
            encryptedPrefs.setURL(response.body()!!.information.profileImgUrl)
            encryptedPrefs.setNumFollowing(response.body()!!.information.numFollowing)
            encryptedPrefs.setNumFollower(response.body()!!.information.numFollower)
            true
        } else {
            Log.e(tag, "내 정보 불러오기 실패")
            false
        }
    }

    override suspend fun doUnLink(token: String): Boolean {
        val response = service.doUnLink("Bearer $token")

        return if(response.isSuccessful && response.body()?.check == true){
            encryptedPrefs.clearAll()
            prefs.clearAll()
            true
        } else {
            Log.e(tag, "회원탈퇴 실패")
            false
        }
    }

    override suspend fun patchNick(token: String, requestBody: EditNickRequest): Boolean {
        val response = service.patchNick("Bearer $token", requestBody)

        return if(response.isSuccessful && response.body()?.check == true){
            encryptedPrefs.setNN(requestBody.nickname)
            true
        } else {
            Log.e(tag, "닉네임 수정 실패")
            false
        }
    }

    override suspend fun getOtherInfo(token: String, userId: Int): Response<MyInfoResponse> {
        val response = service.getOtherInfo("Bearer $token", userId)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun searchUserInfo(
        token: String,
        searchWord: String,
        page:Int
    ): Response<SearchUserResponse> {
        val response = service.searchUserInfo("Bearer $token", searchWord,page)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun patchCalendarOpen(token: String): Response<PatchCalendarOpenResponse> {
        val response = service.patchCalendarOpen("Bearer $token")

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun patchAlarmActive(token: String): Response<PatchAlarmActiveResponse> {
        val response = service.patchAlarmActive("Bearer $token")

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun patchAlarmExhibition(token: String): Response<PatchAlarmExhibitionResponse> {
        val response = service.patchAlarmFollowing("Bearer $token")

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun uploadProfileImg(
        token: String,
        img: MultipartBody.Part
    ): Response<OnlyMsgResponse> {
        val response = service.uploadProfileImg("Bearer $token", img)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }

    override suspend fun getIsOpen(token: String, userId: Int): Response<GetIsOpenResponse> {
        val response = service.getIsOpen("Bearer $token", userId)

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }
    override suspend fun changeFcmToken(token: String, requestBody: ChangeFcmTokenRequest): Boolean {
        val response = service.changeFcmToken("Bearer $token", requestBody)

        return response.isSuccessful && response.body()?.check == true
    }


    override suspend fun getReception(token: String): Response<GetReceptionResponse> {
        val response = service.getReception("Bearer $token")

        return if(response.isSuccessful && response.body()!!.check){
            response
        } else {
            response
        }
    }


}