package com.mayad7474.mazaady_task.data.mapper

import com.mayad7474.mazaady_task.core.BaseMapper
import com.mayad7474.mazaady_task.data.model.properties.PropertyDTO
import com.mayad7474.mazaady_task.doamin.model.properties.Property

object PropertiesMapper : BaseMapper<PropertyDTO, Property> {
    override fun mapTo(item: PropertyDTO) =
        Property(item.id ?: -1, item.slug ?: "", item.options.map { OptionsMapper.mapTo(it) })
}