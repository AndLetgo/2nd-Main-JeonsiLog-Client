package com.example.jeonsilog.view.mypage

data class MyPageInterestModel(
    val id: Int,
    val imgUrl: String,
    val title: String,
    val address: String,
    val keyWord: List<KeyWord>
)

enum class KeyWord {
    before, on, free
}
