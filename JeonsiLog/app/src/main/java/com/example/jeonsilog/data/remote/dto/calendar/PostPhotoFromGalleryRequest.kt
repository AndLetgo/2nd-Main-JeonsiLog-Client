package com.example.jeonsilog.data.remote.dto.calendar

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class PostPhotoFromGalleryRequest (

    @SerializedName("uploadImageReq")
    val uploadImageReq: UploadImageReqEntity,
    @SerializedName("img")
    val img: String,
)

data class UploadImageReqEntity(
    @SerializedName("date")
    val date: String,
    @SerializedName("caption")
    val caption: String
)