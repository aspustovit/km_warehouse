package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.InventoryRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.inventory.InventoryModel

/**
 * Create by Pustovit Oleksandr on 10/08/2026
 */
class GetPartInventoryByFileIdUseCase(private val inventoryRepository: InventoryRepository) :
    UseCase<List<InventoryModel>, Int>() {
    override suspend fun run(params: Int): Result<List<InventoryModel>> {
        return Result.success(inventoryRepository.getPartInventory(params))
    }
}