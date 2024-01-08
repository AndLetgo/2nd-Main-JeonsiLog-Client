package com.example.jeonsilog.data.remote.dto.auth

data class SignUpRequest(
    var providerId: String,
    var nickname: String,
    var email: String,
    var profileImgUrl: String?
)
