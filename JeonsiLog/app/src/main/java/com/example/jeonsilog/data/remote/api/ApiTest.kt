package com.example.jeonsilog.data.remote.api

import com.example.jeonsilog.repository.rating.RatingRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApiTest {
    // android1 로 로그인
    private val token =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3IiwiaWF0IjoxNzAzODMzNzYxLCJleHAiOjE3MDM4MzczNjF9.GWArY1HojFVGAZogIG6QcIj4YUCL2botx4w3O42hbuwPsCkvqmx-Gc69soTXEWvjw0omBtnYARhHfKd52KcSSQ"


    init {
        CoroutineScope(Dispatchers.IO).launch {
            RatingRepositoryImpl().deleteRating(token, 1)
            RatingRepositoryImpl().getMyRatings(token)
        }
    }
}