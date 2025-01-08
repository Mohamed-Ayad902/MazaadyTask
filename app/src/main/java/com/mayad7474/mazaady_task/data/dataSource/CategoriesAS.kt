package com.mayad7474.mazaady_task.data.dataSource

import com.mayad7474.mazaady_task.data.model.categories.CategoriesResponse
import com.mayad7474.mazaady_task.data.model.properties.PropertiesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoriesAS {

    @GET(CATEGORIES)
    suspend fun getCategories(): CategoriesResponse

    @GET(PROPERTIES)
    suspend fun getProperties(@Query("cat") subCatId: Int): PropertiesResponse

    @GET(OPTIONS)
    suspend fun getOptions(@Path("optionId") optionId: Int): PropertiesResponse

    companion object {
        private const val CATEGORIES = "get_all_cats"
        private const val PROPERTIES = "properties"
        private const val OPTIONS = "get-options-child/{optionId}"
    }
}