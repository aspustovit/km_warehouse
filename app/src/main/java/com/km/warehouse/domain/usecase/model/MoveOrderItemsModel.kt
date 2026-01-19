package com.km.warehouse.domain.usecase.model

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
data class MoveOrderItemsModel(
    val id: Int = 0,
    val quantity: Double,
    val qtyGiven: Double,
    val inventoryId: Int,
    val description: String,
    val mfrCode: String,
    val moveOrderId: Int,
    val mfgPartNumExp: String?,
    val noSerials: Boolean,
    val itemSegment: String?
)
