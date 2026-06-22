package com.km.warehouse.domain.usecase.inventory

/**
 * Create by Pustovit Oleksandr on 01/06/2026
 */
data class InventoryModel(
    val inventoryItemId: Long,
    val itemSegment: String,
    val mfgPartNumber: String,
    val itemDescription: String,
    val organizationName: String,
    val subInventoryCode: String,
    val quantity: Double,
    val freeQuantity: Double,
    val fileId: Int = 0
) {
    var factQuantity: Double = 0.0
    var comments: String = ""
}