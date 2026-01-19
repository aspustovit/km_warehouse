package com.km.warehouse.domain.usecase.model

/**
 * Create by Pustovit Oleksandr on 1/12/2026
 */
data class OrderModel(val moveOrderModel: MoveOrderModel, val moveOrderItemsModels: List<MoveOrderItemsModel>)