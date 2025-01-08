package com.mayad7474.mazaady_task.data.mapper

import com.mayad7474.mazaady_task.core.BaseMapper
import com.mayad7474.mazaady_task.data.model.properties.OptionDTO
import com.mayad7474.mazaady_task.doamin.model.options.Option

object OptionsMapper : BaseMapper<OptionDTO, Option> {
    override fun mapTo(item: OptionDTO) =
        Option(item.id ?: -1, item.parent, item.slug ?: "")
}