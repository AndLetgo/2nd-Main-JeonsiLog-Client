package com.example.jeonsilog.data.remote.dto.auth



data class OnlyMsgResponse(
    var check: Boolean,
    var information: Information
)

data class Information(
    val message: String
)
