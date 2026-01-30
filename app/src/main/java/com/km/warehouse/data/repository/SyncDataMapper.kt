package com.km.warehouse.data.repository

import com.km.warehouse.data.entity.ItemsSerial
import com.km.warehouse.data.entity.MoveOrder
import com.km.warehouse.data.entity.MoveOrderItem
import com.km.warehouse.data.network.entity.ItemSerialSync
import com.km.warehouse.data.network.entity.MoveOrderEntity
import com.km.warehouse.data.network.entity.MoveOrderItemsEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Create by Pustovit Oleksandr on 1/9/2026
 */
//val formatter = DateTimeFormatter.ofPattern("YYYY-MM-DDTHH:MM:SS")

fun MoveOrderEntity.toMoveOrderDb(): MoveOrder {
    val createDate = LocalDateTime.parse(creationDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    return MoveOrder(
        id = id,
        description = description,
        creationDate = creationDate,
        moveDate = moveDate,
        number = number,
        bayerId = bayerId,
        status = status,
        scannerId = scannerId,
        isComplete = isComplete
    )
}

fun MoveOrderItemsEntity.toMoveOrderItemDb(): MoveOrderItem {
    return MoveOrderItem(
        id = id,
        moveOrderId = moveOrderId,
        description = description,
        mfrCode = mfgPartNum,
        mfgPartNumExp = mfgPartNumExp,
        itemSegment = itemSegment1,
        noSerials = noSerials,
        inventoryId = inventoryId,
        quantity = quantity,
        qtyGiven = qtyGiven
    )
}

fun ItemsSerial.toItemSerialSync(): ItemSerialSync {
    return ItemSerialSync(moveItemId = moveOrderItemId, serialNumber = serial)
}