package com.km.warehouse.domain.repository

import com.km.warehouse.data.entity.Bayer
import com.km.warehouse.data.network.entity.ErrorData
import com.km.warehouse.domain.usecase.model.ItemSerialModel
import com.km.warehouse.domain.usecase.model.MoveOrderModel
import com.km.warehouse.domain.usecase.model.OrderModel
import com.km.warehouse.domain.usecase.model.QuantityGivenModel
import com.km.warehouse.ui.move_order.DocumentType

/**
 * Create by Pustovit Oleksandr on 9/23/2025
 */
interface LocalWarehouseRepository {
    suspend fun getAllBayer(): List<Bayer>

    suspend fun getMoveOrders(): List<MoveOrderModel>

    suspend fun getBayerMoveOrders(documentType: DocumentType): HashMap<String, List<OrderModel>>

    suspend fun setSerialNumberToDB(seralNumberModel: ItemSerialModel): String

    suspend fun getSerialNumbers(): List<ItemSerialModel>

    suspend fun deleteSerialNumber(seralNumberModel: ItemSerialModel): Int

    suspend fun setQuantityGiven(quantityGivenModel: QuantityGivenModel): Int

    suspend fun setNoSerials(moveOrderItemId: Int): Int

    suspend fun checkSerialAlreadyEnter(serialNumber: String): ErrorData
}