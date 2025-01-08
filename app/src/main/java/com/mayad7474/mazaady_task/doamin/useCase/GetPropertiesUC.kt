package com.mayad7474.mazaady_task.doamin.useCase

import com.mayad7474.mazaady_task.core.utils.safeCallAsFlow
import com.mayad7474.mazaady_task.doamin.repository.ICategoriesRepo
import javax.inject.Inject

class GetPropertiesUC @Inject constructor(private val categoriesRepo: ICategoriesRepo) {

    operator fun invoke(subCatId: Int) = safeCallAsFlow { categoriesRepo.getProperties(subCatId) }
}