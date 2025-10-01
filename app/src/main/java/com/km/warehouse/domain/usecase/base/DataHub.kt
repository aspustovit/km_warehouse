package com.km.warehouse.domain.usecase.base

import android.util.Log
import com.km.warehouse.domain.repository.BarcodeDataEmitter
import com.km.warehouse.domain.repository.BarcodeDataObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Create by Pustovit Oleksandr on 9/26/2025
 */
object DataHub: BarcodeDataEmitter, BarcodeDataObserver {
    private val _barcodeData: MutableSharedFlow<String> = MutableSharedFlow()

    override suspend fun emitBarcodeData(barcode: String) {
        _barcodeData.emit(barcode)
        Log.d("onKeyDown_SCAN_EMIT", barcode)
    }

    override fun observeBarcodeData(): Flow<String> = _barcodeData

}