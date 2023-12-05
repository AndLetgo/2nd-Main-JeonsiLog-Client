package com.example.jeonsilog.data.remote.dto.auth

data class SignUpData(
    var providerId: String,
    var nickname: String,
    var email: String,
    var profileImgUrl: String
)
