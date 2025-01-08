package com.mayad7474.mazaady_task.doamin.useCase

import com.mayad7474.mazaady_task.core.utils.safeCallAsFlow
import com.mayad7474.mazaady_task.doamin.repository.ICategoriesRepo
import javax.inject.Inject

class GetCategoriesUC @Inject constructor(private val categoriesRepo: ICategoriesRepo) {

    operator fun invoke() = safeCallAsFlow { categoriesRepo.getCategories() }
}