package com.km.warehouse.ui.settings

import com.km.warehouse.domain.usecase.inventory.UserWarehouseModel

/**
 * Create by Pustovit Oleksandr on 30/03/2026
 */
data class SettingsState(
    val terminalId: String,
    val searchByMfrCode: Boolean = false,
    val userWarehouseModel: Map<String, List<UserWarehouseModel>> = HashMap()
)
