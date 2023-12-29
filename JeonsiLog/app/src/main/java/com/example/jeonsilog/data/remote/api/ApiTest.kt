package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.repository.follow.FollowRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApiTest {
    // android1 로 로그인(userId = 7)
    private val token1 =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3IiwiaWF0IjoxNzAzODQxNzk3LCJleHAiOjE3MDM4NDUzOTd9.rbfSggm-XV8aAkL9gasQCD855j95K3VtHQ9H4mPxiFezRNGrxSqaITB3oeQlbbZPObeG9gKz76HmzeFW-GFsQg"

    // android2 로 로그인(userId = 8)
    private val token2 =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4IiwiaWF0IjoxNzAzODM4OTE3LCJleHAiOjE3MDM4NDI1MTd9.Y6rAMTx3hDCGEN-l-JWfucaEOTtOFCTsKgMO-l3PXI3r91FQBuliTP8QmINzf-KIwtadgbIFX9SiXiiEu0lhQA"


    init {
        CoroutineScope(Dispatchers.IO).launch {
            FollowRepositoryImpl().deleteFollower(token2, 7)
            FollowRepositoryImpl().getMyFollowing(token1)
            FollowRepositoryImpl().getMyFollower(token1)
        }
    }
}