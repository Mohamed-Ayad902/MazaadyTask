package com.mayad7474.mazaady_task.data.model.categories

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("data") val items: CategoriesDTO,
    @SerializedName("msg") val message: String
)