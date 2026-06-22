package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.InventoryRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.inventory.InventoryModel

/**
 * Create by Pustovit Oleksandr on 19/06/2026
 */
class UpdateFullInventoryModelUseCase(private val inventoryRepository: InventoryRepository) :
    UseCase<Boolean, InventoryModel>() {
    override suspend fun run(params: InventoryModel): Result<Boolean> {
        return Result.success(inventoryRepository.updateInventoryModelInFile(params))
    }
}