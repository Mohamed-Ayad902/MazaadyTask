package com.mayad7474.mazaady_task.data.model.categories

import com.google.gson.annotations.SerializedName

data class CategoryDTO(
    @SerializedName("children") val children: List<SubCategoryDTO>,
    @SerializedName("circle_icon") val circleIcon: String,
    @SerializedName("description") val description: String?,
    @SerializedName("disable_shipping") val disableShipping: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("image") val image: String,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String
)

data class SubCategoryDTO(
    @SerializedName("children") val children: Any?,
    @SerializedName("circle_icon") val circleIcon: String,
    @SerializedName("description") val description: String?,
    @SerializedName("disable_shipping") val disableShipping: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("image") val image: String,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String
)