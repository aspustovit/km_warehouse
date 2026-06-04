package com.km.warehouse.domain.usecase.inventory

/**
 * Create by Pustovit Oleksandr on 01/06/2026
 */
data class UpdateInventoryModel(val inventoryId: Long, val fact: Int, val comments: String)
