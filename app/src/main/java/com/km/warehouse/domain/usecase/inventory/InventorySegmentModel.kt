package com.km.warehouse.domain.usecase.inventory

import com.km.warehouse.data.network.entity.ErrorData

/**
 * Create by Pustovit Oleksandr on 01/06/2026
 */
data class InventorySegmentModel(val inventory: List<InventoryModel>, val errorData: ErrorData?)
