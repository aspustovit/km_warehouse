package com.km.warehouse.domain.usecase.model

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
data class ItemSerialModel(
    val id: Int = 0,
    val serial: String,
    val moveOrderItemId: Int
)
