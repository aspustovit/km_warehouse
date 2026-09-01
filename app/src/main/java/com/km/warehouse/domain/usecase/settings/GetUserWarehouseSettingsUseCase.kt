package com.km.warehouse.domain.usecase.settings

import com.km.warehouse.domain.repository.InventoryRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.inventory.UserWarehouseModel

/**
 * Create by Pustovit Oleksandr on 26/08/2026
 */
class GetUserWarehouseSettingsUseCase(private val inventoryRepository: InventoryRepository) :
    UseCase<HashMap<String, List<UserWarehouseModel>>, Unit>() {
    override suspend fun run(params: Unit): Result<HashMap<String, List<UserWarehouseModel>>> {
        val res = HashMap<String, List<UserWarehouseModel>>()
        inventoryRepository.getUserWarehouses().forEach {
            val warehouses = res[it.warehouseType]?.toMutableList()
            if(warehouses == null) {
                val array = ArrayList<UserWarehouseModel>()
                array.add(it)
                res[it.warehouseType] = array
            } else {
                warehouses.add(it)
                res[it.warehouseType] = warehouses
            }
        }
        return Result.success(res)
    }
}