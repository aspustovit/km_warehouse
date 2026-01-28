package com.km.warehouse.domain.usecase.model

import com.km.warehouse.data.network.entity.ErrorData

/**
 * Create by Pustovit Oleksandr on 1/9/2026
 */
data class SyncResultModel(val isSyncSuccess: Boolean, val errorData: ErrorData? = null) {
}