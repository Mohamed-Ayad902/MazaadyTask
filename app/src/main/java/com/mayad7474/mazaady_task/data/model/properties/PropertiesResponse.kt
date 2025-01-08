package com.mayad7474.mazaady_task.data.model.properties

import com.google.gson.annotations.SerializedName

data class PropertiesResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("data") val items: List<PropertyDTO>,
    @SerializedName("msg") val message: String
)