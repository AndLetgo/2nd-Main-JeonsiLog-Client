package com.example.jeonsilog.data.remote.dto.calendar

import com.google.gson.annotations.SerializedName

data class PostPhotoFromPosterRequest(

    @SerializedName("date")
    val date: String,
    @SerializedName("imgUrl")
    val imgUrl: String,
    @SerializedName("caption")
    val caption: String
)