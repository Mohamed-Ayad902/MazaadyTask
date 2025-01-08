package com.mayad7474.mazaady_task.data.mapper

import com.mayad7474.mazaady_task.core.BaseMapper
import com.mayad7474.mazaady_task.data.model.categories.SubCategoryDTO
import com.mayad7474.mazaady_task.doamin.model.categories.SubCategory

object SubCategoriesMapper : BaseMapper<SubCategoryDTO, SubCategory> {
    override fun mapTo(item: SubCategoryDTO) =
        SubCategory(id = item.id, name = item.slug, isSelected = false)
}