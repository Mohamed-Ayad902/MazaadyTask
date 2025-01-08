package com.mayad7474.mazaady_task.data.model.categories

import com.google.gson.annotations.SerializedName

data class AdsBannerDTO(
    @SerializedName("duration") val duration: Int,
    @SerializedName("img") val image: String,
    @SerializedName("media_type") val mediaType: String
)