package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.LocalWarehouseRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.model.ItemSerialModel

/**
 * Create by Pustovit Oleksandr on 1/15/2026
 */
class SaveSerialToDBUseCase(private val localWarehouseRepository: LocalWarehouseRepository) :
    UseCase<String, ItemSerialModel>() {
    override suspend fun run(params: ItemSerialModel): Result<String> {
        return Result.success(localWarehouseRepository.setSerialNumberToDB(params))
    }
}