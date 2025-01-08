package com.mayad7474.mazaady_task.core

interface BaseMapper<FROM, TO> {
    fun mapTo(item: FROM): TO
}