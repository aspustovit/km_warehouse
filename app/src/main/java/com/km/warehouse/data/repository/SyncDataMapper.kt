package com.km.warehouse.data.repository

import androidx.compose.ui.res.integerResource
import com.km.warehouse.data.entity.Inventory
import com.km.warehouse.data.entity.InventoryFiles
import com.km.warehouse.data.entity.ItemsSerial
import com.km.warehouse.data.entity.MoveOrder
import com.km.warehouse.data.entity.MoveOrderItem
import com.km.warehouse.data.network.entity.InventoryEntity
import com.km.warehouse.data.network.entity.ItemSerialSync
import com.km.warehouse.data.network.entity.MoveOrderEntity
import com.km.warehouse.data.network.entity.MoveOrderItemsEntity
import com.km.warehouse.domain.usecase.inventory.InventoryFileModel
import com.km.warehouse.domain.usecase.inventory.InventoryModel
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

fun MoveOrderItem.toMoveOrderItemSync(): MoveOrderItemsEntity {
    return MoveOrderItemsEntity(
        id = id,
        moveOrderId = moveOrderId,
        quantity = quantity,
        qtyGiven = qtyGiven,
        inventoryId = inventoryId,
        noSerials = noSerials,
        description = description,
        itemSegment1 = itemSegment ?: "",
        mfgPartNum = mfrCode,
        mfgPartNumExp = mfgPartNumExp
    )
}

fun InventoryEntity.toInventoryModel(): InventoryModel {
    return InventoryModel(
        inventoryItemId = inventoryItemId,
        itemSegment = itemSegment,
        mfgPartNumber = mfgPartNumber,
        freeQuantity = freeQuantity,
        quantity = quantity,
        itemDescription = itemDescription,
        subInventoryCode = subInvintoryCode,
        organizationName = organizationName
    )
}

fun Inventory.toInventoryModel(): InventoryModel {
    val im = InventoryModel(
        inventoryItemId = id.toLong(),
        itemSegment = itemSegment,
        mfgPartNumber = inventoryItemId/*mfgPartNumber*/,
        freeQuantity = freeQuantity,
        quantity = quantity,
        itemDescription = itemDescription,
        subInventoryCode = itemSegment,
        organizationName = mfgPartNumber,
        fileId = fileId
    )
    im.comments = comments ?: ""
    im.factQuantity = factQuantity ?: 0.0
    return im
}

fun InventoryFiles.toInventoryFilesModel(): InventoryFileModel {
    return InventoryFileModel(id = id, fileName = fileName, time = createDate, comments = comments)
}

fun InventoryModel.toInventory(): Inventory {
    return Inventory(
        id = inventoryItemId.toInt(),
        itemSegment = itemSegment,
        inventoryItemId = mfgPartNumber /*mfgPartNumber*/,
        freeQuantity = freeQuantity,
        quantity = quantity,
        itemDescription = itemDescription,
        mfgPartNumber = organizationName,
        factQuantity = factQuantity,
        comments = comments,
        fileId = fileId
    )
}