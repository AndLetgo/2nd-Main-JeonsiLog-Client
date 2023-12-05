package com.example.jeonsilog.data.remote.dto.auth

data class IsAvailableResponse(
    val check: Boolean,
    val information: InformationEntity
)

data class InformationEntity(
    val isAvailable: Boolean
)
