package com.km.warehouse.data.repository

import com.km.warehouse.data.KmWarehouseDatabase
import com.km.warehouse.data.entity.Bayer
import com.km.warehouse.data.entity.MoveOrder
import com.km.warehouse.domain.repository.LocalWarehouseRepository
import com.km.warehouse.domain.usecase.model.ItemSerialModel
import com.km.warehouse.domain.usecase.model.MoveOrderModel
import com.km.warehouse.domain.usecase.model.OrderModel
import com.km.warehouse.domain.usecase.toItemSerialDb
import com.km.warehouse.domain.usecase.toMoveOrderItemsModel
import com.km.warehouse.domain.usecase.toMoveOrderModel

/**
 * Create by Pustovit Oleksandr on 9/23/2025
 */
class BayerRepositoryImpl(val database: KmWarehouseDatabase) : LocalWarehouseRepository {
    override suspend fun getAllBayer(): List<Bayer> {
        return database.bayerDao().getBayers()
    }

    override suspend fun getMoveOrders(): List<MoveOrderModel> {
        val bayers = getAllBayer()
        val moveOrders = database.moveOrderDao().getMoveOrders()
        return moveOrders.map { it.toMoveOrderModel(getBayerName(it.bayerId, bayers)) }
    }

    override suspend fun getBayerMoveOrders(): HashMap<String, List<OrderModel>> {
        val bayers = getAllBayer()
        val moveOrders = database.moveOrderDao().getMoveOrders()
        val res = HashMap<String, List<OrderModel>>()
        bayers.forEach {
            val moveOrderBayers = moveOrders.filter { m -> m.bayerId == it.id }
            val orderModelList = ArrayList<OrderModel>()
            moveOrderBayers.forEach { moveOrder ->
                val orderItems =
                    database.moveOrderItemDao().getMoveOrderItemsByOrderId(moveOrder.id)
                orderModelList.add(
                    OrderModel(
                        moveOrderModel = moveOrder.toMoveOrderModel(it.description),
                        moveOrderItemsModels = orderItems.map { items -> items.toMoveOrderItemsModel() })
                )
            }
            res.put(it.description, orderModelList)
        }
        return res
    }

    override suspend fun setSerialNumberToDB(seralNumberModel: ItemSerialModel): String {
        try {
            database.itemsSerialDao().insert(seralNumberModel.toItemSerialDb())
            return ""
        } catch (ex: Exception){
            return ex.toString()
        }

    }

    private fun getBayerName(bayerId: Int, bayers: List<Bayer>): String {
        var bayer = bayers.find { it.id == bayerId }
        if (bayer == null)
            bayer = bayers.find { it.id == -1 }
        return bayer!!.description
    }
}