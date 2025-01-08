package com.mayad7474.mazaady_task.data.mapper

import com.mayad7474.mazaady_task.core.BaseMapper
import com.mayad7474.mazaady_task.data.model.categories.CategoryDTO
import com.mayad7474.mazaady_task.doamin.model.categories.Category

object CategoriesMapper : BaseMapper<CategoryDTO, Category> {
    override fun mapTo(item: CategoryDTO) =
        Category(
            id = item.id,
            name = item.slug,
            isSelected = false,
            subCategories = item.children.map { SubCategoriesMapper.mapTo(it) })
}