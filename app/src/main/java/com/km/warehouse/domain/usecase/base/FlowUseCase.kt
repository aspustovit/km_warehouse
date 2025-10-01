package com.km.warehouse.domain.usecase.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn

/**
 * Create by Pustovit Oleksandr on 9/26/2025
 */
abstract class FlowUseCase<out OUT,IN>(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) where OUT : Any {

    protected val channel =
        MutableSharedFlow<IN>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    open operator fun invoke(params: IN) {
        channel.tryEmit(params)
    }

    protected abstract fun createFlow(params: IN): Flow<OUT>

    open fun observe(): Flow<OUT> = channel.flatMapLatest { params ->
        createFlow(params).flowOn(dispatcher)
    }
}