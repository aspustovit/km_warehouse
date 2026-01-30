package com.km.warehouse.ui.sync

import com.km.warehouse.data.entity.Bayer

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
data class SyncState(
    val syncStatus: SyncStatus,
    val syncError: String? = null,
    val documentForSyncCount: Int = 0
)