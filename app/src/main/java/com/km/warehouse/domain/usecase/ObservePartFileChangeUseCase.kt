package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.usecase.base.DataHub
import com.km.warehouse.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow

/**
 * Create by Pustovit Oleksandr on 11/08/2026
 */
class ObservePartFileChangeUseCase: FlowUseCase<Boolean, Unit>() {
    override fun createFlow(params: Unit): Flow<Boolean> {
        return DataHub.observeSavedBarcodeFileChange()
    }
}