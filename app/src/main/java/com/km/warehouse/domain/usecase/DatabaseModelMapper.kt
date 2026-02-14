package com.km.warehouse.domain.usecase

import com.km.warehouse.data.entity.ItemsSerial
import com.km.warehouse.data.entity.MoveOrder
import com.km.warehouse.data.entity.MoveOrderItem
import com.km.warehouse.domain.usecase.model.ItemSerialModel
import com.km.warehouse.domain.usecase.model.MoveOrderItemsModel
import com.km.warehouse.domain.usecase.model.MoveOrderModel

/**
 * Create by Pustovit Oleksandr on 1/10/2026
 */

fun MoveOrder.toMoveOrderModel(bayerName: String): MoveOrderModel {
    return MoveOrderModel(
        id = id,
        description = description,
        creationDate = creationDate,
        moveDate = moveDate,
        number = number,
        bayerName = bayerName,
        bayerId = bayerId,
        status = status,
        isComplete = isComplete == "Y"
    )
}

fun MoveOrderItem.toMoveOrderItemsModel(): MoveOrderItemsModel {
    return MoveOrderItemsModel(
        id = id,
        moveOrderId = moveOrderId,
        quantity = quantity,
        qtyGiven = qtyGiven,
        inventoryId = inventoryId,
        description = description,
        mfrCode = mfrCode,
        mfgPartNumExp = mfgPartNumExp,
        noSerials = noSerials == "Y",
        itemSegment = itemSegment
    )
}

fun ItemSerialModel.toItemSerialDb(): ItemsSerial {
    return ItemsSerial(serial = serial, moveOrderItemId = moveOrderItemId)
}

fun ItemsSerial.toItemSerialModel(): ItemSerialModel {
    return ItemSerialModel(serial = serial, moveOrderItemId = moveOrderItemId, id = id)
}