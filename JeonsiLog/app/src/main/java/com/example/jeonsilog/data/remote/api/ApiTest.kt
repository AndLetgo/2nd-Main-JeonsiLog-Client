package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.repository.exhibition.ExhibitionRepositoryImpl
import com.example.jeonsilog.widget.utils.GlobalApplication.Companion.encryptedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApiTest {
    // android1 로 로그인(userId = 7)
    private val token1 =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3IiwiaWF0IjoxNzAzODQ1NjQxLCJleHAiOjE3MDM4NDkyNDF9.EO3nzb1PLP_hdns3VTZ7OqZkcbinIVDM0tb2eZx1Z0r7fubnAKEkTZQPuasmGKc92ND_YoVV3y1MnEmKZhiqbg"

    // android2 로 로그인(userId = 8)
    private val token2 =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4IiwiaWF0IjoxNzAzODQ1NjU4LCJleHAiOjE3MDM4NDkyNTh9.30Pu3UvURotODkGfic-uin_V6eXulqPUd0ba80ZKQV4nEriO8naAkOCnVYRvYUM0O5zOW_SmCgUIHeEtg4NL5A"

    init {
        println("${encryptedPrefs.getRT()} / ${encryptedPrefs.getAT()}")

        CoroutineScope(Dispatchers.IO).launch {
            val response = ExhibitionRepositoryImpl().getExhibition(encryptedPrefs.getAT(), 1)

            if(response.isSuccessful && response.body()!!.check){

            } else {

            }
        }
    }
}