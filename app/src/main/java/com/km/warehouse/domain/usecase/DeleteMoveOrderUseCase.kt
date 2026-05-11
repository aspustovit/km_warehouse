package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.LocalWarehouseRepository
import com.km.warehouse.domain.usecase.base.UseCase

class DeleteMoveOrderUseCase(private val localWarehouseRepository: LocalWarehouseRepository) :
    UseCase<Int, Int>() {
    override suspend fun run(params: Int): Result<Int> {
        return Result.success(localWarehouseRepository.deleteMoveOrder(params))
    }
}