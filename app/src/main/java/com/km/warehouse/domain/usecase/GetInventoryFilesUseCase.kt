package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.InventoryRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.inventory.InventoryFileModel

/**
 * Create by Pustovit Oleksandr on 08/06/2026
 */
class GetInventoryFilesUseCase(private val inventoryRepository: InventoryRepository) :
    UseCase<List<InventoryFileModel>, Unit>() {
    override suspend fun run(params: Unit): Result<List<InventoryFileModel>> {
        return Result.success(inventoryRepository.getInventoryFiles())
    }
}