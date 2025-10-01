package com.km.warehouse.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Create by Pustovit Oleksandr on 9/26/2025
 */
interface BarcodeDataEmitter {
    suspend fun emitBarcodeData(barcode: String)
}


interface BarcodeDataObserver {
    fun observeBarcodeData(): Flow<String>
}