package com.mayad7474.mazaady_task.doamin.model.properties

import com.mayad7474.mazaady_task.doamin.model.options.Option

data class Property(
    val id: Int,
    val name: String,
    val options: List<Option>,
    var otherOption: String? = null
)
