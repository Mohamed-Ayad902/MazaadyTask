package com.mayad7474.mazaady_task.doamin.model.options

import com.mayad7474.mazaady_task.doamin.model.categories.SubCategory

data class Option(
    val id: Int,
    val parentId: Int?,
    val name: String,
    var isSelected: Boolean = false
)

fun Option.toSubCat() = SubCategory(parentId ?: -1, name, isSelected)