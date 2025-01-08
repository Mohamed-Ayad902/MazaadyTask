package com.mayad7474.mazaady_task.data.model.properties

import com.google.gson.annotations.SerializedName

data class OptionDTO(
    @SerializedName("child") val child: Boolean?,
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("parent") val parent: Int?,
    @SerializedName("slug") val slug: String?
)