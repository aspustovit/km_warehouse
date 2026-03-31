package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.LocalWarehouseRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.model.ItemSerialModel

/**
 * Create by Pustovit Oleksandr on 31/03/2026
 */
class UpdateSerialNumberUseCase(private val localWarehouseRepository: LocalWarehouseRepository) :
    UseCase<Int, Pair<ItemSerialModel, String>>() {
    override suspend fun run(params: Pair<ItemSerialModel, String>): Result<Int> {
        return Result.success(
            localWarehouseRepository.updateSerial(
                serialNumber = params.second,
                prevSeralNumberModel = params.first
            )
        )
    }
}