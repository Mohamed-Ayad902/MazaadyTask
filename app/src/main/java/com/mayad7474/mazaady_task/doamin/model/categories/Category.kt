package com.mayad7474.mazaady_task.doamin.model.categories

import com.mayad7474.mazaady_task.doamin.model.options.Option

data class Category(
    val id: Int,
    val name: String,
    val isSelected: Boolean,
    val subCategories: List<SubCategory>,
)

data class SubCategory(
    val id: Int,
    val name: String,
    val isSelected: Boolean,
)

fun Category.toOption() = Option(id = id, parentId = id, name = name, isSelected = isSelected)
fun SubCategory.toOption() = Option(id = id, parentId = id, name = name, isSelected = isSelected)