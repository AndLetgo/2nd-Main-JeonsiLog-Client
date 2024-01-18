package com.example.jeonsilog.data.remote.dto.exhibition

import com.google.gson.annotations.SerializedName

data class PatchExhibitionSequenceRequest(
    @SerializedName("updateSequenceInfo")
    var updateSequenceInfo: MutableList<UpdateSequenceInfo>
)

data class UpdateSequenceInfo(
    @SerializedName("sequence")
    val sequence: Int,
    @SerializedName("exhibitionId")
    val exhibitionId: Int
)