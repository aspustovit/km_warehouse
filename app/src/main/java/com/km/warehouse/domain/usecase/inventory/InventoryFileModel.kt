package com.km.warehouse.domain.usecase.inventory

/**
 * Create by Pustovit Oleksandr on 08/06/2026
 */
data class InventoryFileModel(
    val id: Int,
    val fileName: String,
    val time: Long,
    val comments: String? = null
)