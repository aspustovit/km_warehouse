package com.km.warehouse.domain.usecase

import android.util.Log
import com.km.warehouse.domain.usecase.base.DataHub
import com.km.warehouse.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow

/**
 * Create by Pustovit Oleksandr on 9/26/2025
 */
class ObserveBarcodeDataUseCase: FlowUseCase<String, Unit>() {
    override fun createFlow(params: Unit): Flow<String> {
        Log.d("onKeyDown_SCAN_CF", "it")
        return DataHub.observeBarcodeData()
    }
}