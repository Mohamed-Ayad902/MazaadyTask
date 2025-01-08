package com.mayad7474.mazaady_task.data.model.properties

import com.google.gson.annotations.SerializedName

data class PropertyDTO(
    @SerializedName("description") val description: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("list") val isList: Boolean?,
    @SerializedName("name") val name: String?,
    @SerializedName("options") val options: List<OptionDTO>,
    @SerializedName("other_value") val otherValue: Any?,
    @SerializedName("parent") val parent: Any?,
    @SerializedName("slug") val slug: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("value") val value: String?
)