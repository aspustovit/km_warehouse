package com.km.warehouse.domain.usecase.model

/**
 * Create by Pustovit Oleksandr on 1/9/2026
 */
data class SyncResultModel(val isSyncSuccess: Boolean, val errorMessage: String = "", val errorCode: Int = 200) {
}