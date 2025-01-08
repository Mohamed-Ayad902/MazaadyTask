package com.mayad7474.mazaady_task.data.model.categories

import com.google.gson.annotations.SerializedName

data class StatisticsDTO(
    @SerializedName("auctions") val auctions: Int,
    @SerializedName("products") val products: Int,
    @SerializedName("users") val users: Int
)