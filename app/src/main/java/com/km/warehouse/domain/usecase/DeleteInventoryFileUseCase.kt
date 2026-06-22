package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.InventoryRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.inventory.InventoryFileModel

/**
 * Create by Pustovit Oleksandr on 17/06/2026
 */
class DeleteInventoryFileUseCase(private val inventoryRepository: InventoryRepository) :
    UseCase<Boolean, InventoryFileModel>() {
    override suspend fun run(params: InventoryFileModel): Result<Boolean> {
        return Result.success(inventoryRepository.deleteInventoryFile(params.id))
    }
}