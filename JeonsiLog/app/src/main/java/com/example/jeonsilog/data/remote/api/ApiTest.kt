package com.example.jeonsilog.data.remote.api

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
        CoroutineScope(Dispatchers.IO).launch {
        }
    }
}