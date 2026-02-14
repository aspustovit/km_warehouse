package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.LocalWarehouseRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.model.MoveOrderModel
import com.km.warehouse.domain.usecase.model.OrderModel
import com.km.warehouse.ui.move_order.DocumentType

/**
 * Create by Pustovit Oleksandr on 1/11/2026
 */
class LoadMoveOrdersUseCase(private val warehouseRepository: LocalWarehouseRepository) :
    UseCase<HashMap<String, List<OrderModel>>, DocumentType>() {
    override suspend fun run(params: DocumentType): Result<HashMap<String, List<OrderModel>>> {
        return Result.success(warehouseRepository.getBayerMoveOrders(params))
    }
}