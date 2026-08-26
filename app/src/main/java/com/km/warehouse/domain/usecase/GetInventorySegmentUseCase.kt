package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.InventoryRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.inventory.InventorySegmentModel

/**
 * Create by Pustovit Oleksandr on 01/06/2026
 */
class GetInventorySegmentUseCase(private val inventoryRepository: InventoryRepository) :
    UseCase<InventorySegmentModel, Pair<String, Int>>() {
    override suspend fun run(params: Pair<String, Int>): Result<InventorySegmentModel> {
        return Result.success(inventoryRepository.loadInventoryBySegment(params))
    }
}