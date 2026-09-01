package com.km.warehouse.domain.usecase.settings

import com.km.warehouse.domain.repository.InventoryRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.inventory.UserWarehouseModel

/**
 * Create by Pustovit Oleksandr on 26/08/2026
 */
class SetUserWarehouseSettingsUseCase(private val inventoryRepository: InventoryRepository) :
    UseCase<Boolean, List<UserWarehouseModel>>() {
    override suspend fun run(params: List<UserWarehouseModel>): Result<Boolean> {
        return Result.success(inventoryRepository.saveUserWarehouses(params))
    }
}