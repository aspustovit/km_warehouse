package com.km.warehouse.domain.usecase

import com.km.warehouse.data.converter.InventoryFileTypes
import com.km.warehouse.domain.repository.InventoryRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.inventory.InventoryFileModel

/**
 * Create by Pustovit Oleksandr on 08/06/2026
 */
class GetInventoryFilesUseCase(private val inventoryRepository: InventoryRepository) :
    UseCase<List<InventoryFileModel>, InventoryFileTypes>() {
    override suspend fun run(params: InventoryFileTypes): Result<List<InventoryFileModel>> {
        return when(params){
            InventoryFileTypes.FULL -> Result.success(inventoryRepository.getInventoryFiles())
            InventoryFileTypes.PART -> Result.success(inventoryRepository.getPartInventoryFromDB())
        }
    }
}