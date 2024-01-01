package com.example.jeonsilog.view.notification

data class NotificationModel(
    val userId: Long,
    val exhibitionId: Long,
    val profileImg: String,
    val title: String,
    val content: String,
    val time: String,
    val type: Int
)
