package com.mayad7474.mazaady_task.data.model.categories

import com.google.gson.annotations.SerializedName

data class CategoriesDTO(
    @SerializedName("ads_banners") val adsBanners: List<AdsBannerDTO>,
    @SerializedName("categories") val categories: List<CategoryDTO>,
    @SerializedName("google_version") val googleVersion: String,
    @SerializedName("huawei_version") val huaweiVersion: String,
    @SerializedName("ios_latest_version") val iosLatestVersion: String,
    @SerializedName("ios_version") val iosVersion: String,
    @SerializedName("statistics") val statistics: StatisticsDTO
)