package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.repository.interest.InterestRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApiTest {
    // android1 로 로그인(userId = 7)
    private val token1 =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3IiwiaWF0IjoxNzAzODM4MDYzLCJleHAiOjE3MDM4NDE2NjN9.NfqI6ncdp0SU8ho8tMJZmEFtY4zwYfEnBGo9JpUbMy6ckdyoQNryiFGOHl22VyZzVuiwQ-XRP5DBzHiBErzQpA"

    // android2 로 로그인(userId = 8)
    private val token2 =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4IiwiaWF0IjoxNzAzODM4OTE3LCJleHAiOjE3MDM4NDI1MTd9.Y6rAMTx3hDCGEN-l-JWfucaEOTtOFCTsKgMO-l3PXI3r91FQBuliTP8QmINzf-KIwtadgbIFX9SiXiiEu0lhQA"


    init {
        CoroutineScope(Dispatchers.IO).launch {
            InterestRepositoryImpl().deleteInterest(token1, 1)
            InterestRepositoryImpl().getInterest(token1)
        }
    }
}