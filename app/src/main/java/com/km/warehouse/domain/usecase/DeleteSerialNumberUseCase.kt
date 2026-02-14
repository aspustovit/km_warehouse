package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.LocalWarehouseRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.model.ItemSerialModel

/**
 * Create by Pustovit Oleksandr on 2/9/2026
 */
class DeleteSerialNumberUseCase(private val localWarehouseRepository: LocalWarehouseRepository) :
    UseCase<Int, ItemSerialModel>() {
    override suspend fun run(params: ItemSerialModel): Result<Int> {
        return Result.success(localWarehouseRepository.deleteSerialNumber(params))
    }

}