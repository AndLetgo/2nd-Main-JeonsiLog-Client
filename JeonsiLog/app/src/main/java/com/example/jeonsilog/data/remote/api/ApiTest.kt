package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.data.remote.dto.exhibition.PatchExhibitionRequest
import com.example.jeonsilog.data.remote.dto.exhibition.UpdatePlaceInfoEntity
import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApiTest {
    // android1 로 로그인
    private val token =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3IiwiaWF0IjoxNzAzNzg4NDQ5LCJleHAiOjE3MDM3OTIwNDl9.zVsWq4l-9F-z_f3DkGXi4r8Ra32byvjciIrMsF5KIf_Ige1WomuolYYMdMnJcmoQQiBslsrkrCtLmD2Riu5DZA"

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val body = PatchExhibitionRequest(
                1,
                "한국필묵그룹 선흔 창립20주년 기념전",
                "ON_DISPLAY",
                "FREE",
                "이 전시회는 어떤 전시회입니다.",
                UpdatePlaceInfoEntity(
                    1,
                    "서울특별시 송파구 올림픽로 300 롯데월드몰 8층 롯데콘서트홀",
                    "~~~ ~~~",
                    "MONDAY",
                    "031-590-4358",
                    "\thttps://culture.nyj.go.kr/home/"
                )
            )

            ExhibitionRepositoryImpl().patchExhibition(token, body)
        }
    }
}