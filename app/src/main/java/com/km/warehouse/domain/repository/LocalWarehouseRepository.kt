package com.km.warehouse.domain.repository

import com.km.warehouse.data.entity.Bayer
import com.km.warehouse.domain.usecase.model.ItemSerialModel
import com.km.warehouse.domain.usecase.model.MoveOrderModel
import com.km.warehouse.domain.usecase.model.OrderModel

/**
 * Create by Pustovit Oleksandr on 9/23/2025
 */
interface LocalWarehouseRepository {
    suspend fun getAllBayer(): List<Bayer>

    suspend fun getMoveOrders(): List<MoveOrderModel>

    suspend fun getBayerMoveOrders(): HashMap<String, List<OrderModel>>

    suspend fun setSerialNumberToDB(seralNumberModel: ItemSerialModel): String
}