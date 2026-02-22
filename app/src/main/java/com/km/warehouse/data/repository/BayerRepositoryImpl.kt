package com.km.warehouse.data.repository

import android.util.Log
import com.km.warehouse.data.KmWarehouseDatabase
import com.km.warehouse.data.entity.Bayer
import com.km.warehouse.data.entity.MoveOrder
import com.km.warehouse.data.network.entity.ErrorData
import com.km.warehouse.domain.repository.LocalWarehouseRepository
import com.km.warehouse.domain.usecase.model.ItemSerialModel
import com.km.warehouse.domain.usecase.model.MoveOrderModel
import com.km.warehouse.domain.usecase.model.OrderModel
import com.km.warehouse.domain.usecase.model.QuantityGivenModel
import com.km.warehouse.domain.usecase.toItemSerialDb
import com.km.warehouse.domain.usecase.toItemSerialModel
import com.km.warehouse.domain.usecase.toMoveOrderItemsModel
import com.km.warehouse.domain.usecase.toMoveOrderModel
import com.km.warehouse.ui.move_order.DocumentType

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

    override suspend fun getBayerMoveOrders(documentType: DocumentType): HashMap<String, List<OrderModel>> {
        val bayers = getAllBayer()
        val moveOrders = database.moveOrderDao().getMoveOrders(documentType.code)
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
            if(orderModelList.isNotEmpty())
                res.put(it.description, orderModelList)
        }
        return res
    }

    override suspend fun setSerialNumberToDB(seralNumberModel: ItemSerialModel): String {
        try {
            val moveOrderItem = database.moveOrderItemDao().getMoveOrderItem(seralNumberModel.moveOrderItemId)
            database.itemsSerialDao().insert(seralNumberModel.toItemSerialDb())
            moveOrderItem?.let {
                val updateItem = it.copy(qtyGiven = it.qtyGiven+1)
                database.moveOrderItemDao().update(updateItem)
            }
            return ""
        } catch (ex: Exception){
            return ex.toString()
        }

    }

    override suspend fun getSerialNumbers(): List<ItemSerialModel> {
        return database.itemsSerialDao().getSerialsToSync().map { it.toItemSerialModel() }
    }

    override suspend fun deleteSerialNumber(seralNumberModel: ItemSerialModel): Int {
        val result = database.itemsSerialDao().deleteBySerial(seralNumberModel.serial)
        if(result > 0) {
            val moveOrderItem = database.moveOrderItemDao().getMoveOrderItem(seralNumberModel.moveOrderItemId)
            moveOrderItem?.let {
                val updateItem = it.copy(qtyGiven = it.qtyGiven-1)
                database.moveOrderItemDao().update(updateItem)
            }
        }
        return result
    }

    override suspend fun setQuantityGiven(quantityGivenModel: QuantityGivenModel): Int {
        var result = 0
        val moveOrderItem = database.moveOrderItemDao().getMoveOrderItem(quantityGivenModel.moveOrderItemId)
        moveOrderItem?.let {
            val updateItem = it.copy(qtyGiven = quantityGivenModel.quantityGiven.toDouble())
            result = database.moveOrderItemDao().update(updateItem)
        }
        return result
    }

    override suspend fun setNoSerials(moveOrderItemId: Int): Int {
        var result = 0
        val moveOrderItem = database.moveOrderItemDao().getMoveOrderItem(moveOrderItemId)
        moveOrderItem?.let {
            val updateItem = it.copy(noSerials = "Y")
            result = database.moveOrderItemDao().update(updateItem)
        }
        return result
    }

    override suspend fun checkSerialAlreadyEnter(serialNumber: String): ErrorData {
        val serial = database.itemsSerialDao().checkSerialAlreadyAdded(serialNumber)
        Log.d("checkSerialAlreadyEnter", "$serialNumber = $serial")
        if(serial != null) {
            val moi = database.moveOrderItemDao().getMoveOrderItem(serial.moveOrderItemId)
            return ErrorData(1001, "Серійний номер вже додано в документ! ${moi?.mfrCode} - Серійний номер:${serial.serial}", "Повторення серійного номеру")
        }
        return ErrorData(status = 0, message = "", error = "")
    }

    private fun getBayerName(bayerId: Int, bayers: List<Bayer>): String {
        var bayer = bayers.find { it.id == bayerId }
        if (bayer == null)
            bayer = bayers.find { it.id == -1 }
        return bayer!!.description
    }
}