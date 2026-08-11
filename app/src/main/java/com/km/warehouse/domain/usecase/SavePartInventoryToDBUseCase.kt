package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.InventoryRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.inventory.InventoryModel

/**
 * Create by Pustovit Oleksandr on 10/08/2026
 */
class SavePartInventoryToDBUseCase(private val inventoryRepository: InventoryRepository) :
    UseCase<Int, Pair<String,List<InventoryModel>>>() {
    override suspend fun run(params: Pair<String, List<InventoryModel>>): Result<Int> {
        return Result.success(inventoryRepository.savePartInventoryToDB(params))
    }

}