package com.mayad7474.mazaady_task.presentation.category

import com.mayad7474.mazaady_task.core.exceptions.CustomException
import com.mayad7474.mazaady_task.doamin.model.categories.Category
import com.mayad7474.mazaady_task.doamin.model.properties.Property

sealed class CategoriesUIState {
    data object Idle : CategoriesUIState()
    data class Loading(val isLoading: Boolean) : CategoriesUIState()
    data class LoadedCategories(val categories: List<Category>) : CategoriesUIState()
    data class LoadedProperties(val properties: List<Property>) : CategoriesUIState()
    data class Failure(val exception: CustomException) : CategoriesUIState()
}