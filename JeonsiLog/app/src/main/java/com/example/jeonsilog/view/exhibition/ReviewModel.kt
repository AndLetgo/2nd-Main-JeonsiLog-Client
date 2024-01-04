package com.example.jeonsilog.view.exhibition

data class ReviewModel(
    val userId: Long,
    val exhibitionId: Long,
    val contents: String,
    val numReply: Int,
    val ceatedDate: String
)
