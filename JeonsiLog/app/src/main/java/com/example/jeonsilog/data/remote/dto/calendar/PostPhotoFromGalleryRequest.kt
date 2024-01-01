package com.example.jeonsilog.data.remote.dto.calendar

import com.google.gson.annotations.SerializedName

data class PostPhotoFromGalleryRequest (

    @SerializedName("uploadImageReq")
    val uploadImageReq: UploadImageReqEntity
)

data class UploadImageReqEntity(
    @SerializedName("date")
    val date: String
)