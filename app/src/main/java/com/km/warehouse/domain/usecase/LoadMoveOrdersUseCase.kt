package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.LocalWarehouseRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.model.MoveOrderModel
import com.km.warehouse.domain.usecase.model.OrderModel

/**
 * Create by Pustovit Oleksandr on 1/11/2026
 */
class LoadMoveOrdersUseCase(private val warehouseRepository: LocalWarehouseRepository) :
    UseCase<HashMap<String, List<OrderModel>>, Unit>() {
    override suspend fun run(params: Unit): Result<HashMap<String, List<OrderModel>>> {
        return Result.success(warehouseRepository.getBayerMoveOrders())
    }
}