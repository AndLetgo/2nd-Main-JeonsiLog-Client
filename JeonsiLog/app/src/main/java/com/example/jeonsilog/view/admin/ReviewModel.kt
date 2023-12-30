package com.example.jeonsilog.view.admin

data class ReviewModel(
    val userId: Long,
    val exhibitionId: Long,
    val contents: String,
    val numReply: Int,
    val ceatedDate: String
)