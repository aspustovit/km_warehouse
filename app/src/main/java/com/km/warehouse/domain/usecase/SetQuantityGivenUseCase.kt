package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.LocalWarehouseRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.model.QuantityGivenModel

/**
 * Create by Pustovit Oleksandr on 2/9/2026
 */
class SetQuantityGivenUseCase(private val localWarehouseRepository: LocalWarehouseRepository) :
    UseCase<Int, QuantityGivenModel>() {
    override suspend fun run(params: QuantityGivenModel): Result<Int> {
        return Result.success(localWarehouseRepository.setQuantityGiven(params))
    }
}