package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.LocalWarehouseRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.model.ItemSerialModel

/**
 * Create by Pustovit Oleksandr on 2/5/2026
 */
class GetItemSerialFromDBUseCase(private val localWarehouseRepository: LocalWarehouseRepository) :
    UseCase<List<ItemSerialModel>, Unit>()  {
    override suspend fun run(params: Unit): Result<List<ItemSerialModel>> {
        return Result.success(localWarehouseRepository.getSerialNumbers())
    }
}