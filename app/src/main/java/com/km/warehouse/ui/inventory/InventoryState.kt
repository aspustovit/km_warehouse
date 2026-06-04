package com.km.warehouse.ui.inventory

import com.km.warehouse.data.network.entity.ErrorData
import com.km.warehouse.domain.usecase.inventory.InventoryModel

/**
 * Create by Pustovit Oleksandr on 01/06/2026
 */
data class InventoryState(
    val inventoryBarcode: String = "",
    val inventoryListLoading: Boolean = false,
    val inventory: List<InventoryModel>,
    val errorData: ErrorData? = null,
    val error: String? = null,
    val selectedInventory: InventoryModel? = null,
    val showExitDialog: Boolean = false
)