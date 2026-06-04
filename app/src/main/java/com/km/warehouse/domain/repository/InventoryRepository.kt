package com.km.warehouse.domain.repository

import com.km.warehouse.domain.usecase.inventory.InventorySegmentModel

/**
 * Create by Pustovit Oleksandr on 01/06/2026
 */
interface InventoryRepository {
    suspend fun loadInventoryBySegment(itemSegment: String): InventorySegmentModel
}