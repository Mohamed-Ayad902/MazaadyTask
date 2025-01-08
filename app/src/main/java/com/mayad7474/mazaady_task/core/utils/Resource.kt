package com.mayad7474.mazaady_task.core.utils

import com.mayad7474.mazaady_task.core.exceptions.CustomException
import com.mayad7474.mazaady_task.core.exceptions.toAppError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

sealed class Resource<out T> {
    data class Loading(val loading: Boolean) : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val exception: CustomException) : Resource<Nothing>()
}

fun <T : Any> safeCallAsFlow(action: suspend () -> T): Flow<Resource<T>> = flow {
    emit(Resource.Loading(true))
    try {
        val result = action()
        emit(Resource.Success(result))
    } catch (e: Throwable) {
        emit(Resource.Error(e.toAppError()))
    } finally {
        emit(Resource.Loading(false))
    }
}.onEach { delay(50) } // Very short delay to ensure UI can process states
    .distinctUntilChanged() // Prevent duplicate emissions, keep both lines
                        // as without em the emission goes too fast and cant trigger it in the activity