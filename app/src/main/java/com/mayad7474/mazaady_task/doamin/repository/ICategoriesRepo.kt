package com.mayad7474.mazaady_task.doamin.repository

import com.mayad7474.mazaady_task.doamin.model.categories.Category
import com.mayad7474.mazaady_task.doamin.model.properties.Property

interface ICategoriesRepo {
    suspend fun getCategories(): List<Category>
    suspend fun getProperties(subCatId: Int): List<Property>
    suspend fun getOptionsForProperty(propertyId: Int): List<Property>
}