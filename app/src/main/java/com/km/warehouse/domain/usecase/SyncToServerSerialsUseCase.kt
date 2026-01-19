package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.SyncWarehouseRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.model.SyncResultModel

/**
 * Create by Pustovit Oleksandr on 1/15/2026
 */
class SyncToServerSerialsUseCase(private val syncWarehouseRepository: SyncWarehouseRepository) :
    UseCase<SyncResultModel, Unit>() {
    override suspend fun run(params: Unit): Result<SyncResultModel> {
        return Result.success(syncWarehouseRepository.syncToServerSerialsData())
    }
}