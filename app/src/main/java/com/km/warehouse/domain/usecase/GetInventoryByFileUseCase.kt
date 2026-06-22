package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.InventoryRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.inventory.InventorySegmentModel

/**
 * Create by Pustovit Oleksandr on 08/06/2026
 */
class GetInventoryByFileUseCase(private val inventoryRepository: InventoryRepository) :
    UseCase<InventorySegmentModel, Pair<Int, String>>() {
    override suspend fun run(params: Pair<Int, String>): Result<InventorySegmentModel> {
        return Result.success(inventoryRepository.geInventoryByFile(params.first, params.second))
    }
}