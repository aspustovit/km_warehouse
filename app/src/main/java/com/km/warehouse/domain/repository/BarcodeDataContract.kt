package com.km.warehouse.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Create by Pustovit Oleksandr on 9/26/2025
 */
interface BarcodeDataEmitter {
    suspend fun emitBarcodeData(barcode: String)
    suspend fun emitSavedBarcodeFileChange(isChange: Boolean)
}


interface BarcodeDataObserver {
    fun observeBarcodeData(): Flow<String>
    fun observeSavedBarcodeFileChange(): Flow<Boolean>
}