package com.km.warehouse.domain.usecase.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Create by Pustovit Oleksandr on 9/24/2025
 */
/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 * <p>
 * By convention each UseCase implementation will return the result using a coroutine
 * that will execute its job in a background thread and will post the result in the UI thread.
 */
abstract class UseCase<out OUT, in IN>(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) where OUT : Any {

    protected abstract suspend fun run(params: IN): Result<OUT>

    suspend operator fun invoke(params: IN): Result<OUT> = runCatching {
        withContext(dispatcher) {
            run(params).getOrThrow()
        }
    }

}