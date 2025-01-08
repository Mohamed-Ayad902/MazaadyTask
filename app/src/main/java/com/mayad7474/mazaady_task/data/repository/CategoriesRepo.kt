package com.mayad7474.mazaady_task.data.repository

import com.mayad7474.mazaady_task.data.dataSource.CategoriesAS
import com.mayad7474.mazaady_task.data.mapper.CategoriesMapper
import com.mayad7474.mazaady_task.data.mapper.PropertiesMapper
import com.mayad7474.mazaady_task.doamin.model.categories.Category
import com.mayad7474.mazaady_task.doamin.model.properties.Property
import com.mayad7474.mazaady_task.doamin.repository.ICategoriesRepo
import javax.inject.Inject

class CategoriesRepo @Inject constructor(private val categoriesAS: CategoriesAS) : ICategoriesRepo {

    override suspend fun getCategories(): List<Category> {
        return categoriesAS.getCategories().items.categories.mapIndexed { index, categoryDTO ->
            CategoriesMapper.mapTo(categoryDTO).copy(isSelected = index == 0)
        }
    }

    override suspend fun getProperties(subCatId: Int): List<Property> {
        return categoriesAS.getProperties(subCatId).items.map { PropertiesMapper.mapTo(it) }
    }

    override suspend fun getOptionsForProperty(propertyId: Int): List<Property> {
        return categoriesAS.getOptions(propertyId).items.map { PropertiesMapper.mapTo(it) }
    }

}
