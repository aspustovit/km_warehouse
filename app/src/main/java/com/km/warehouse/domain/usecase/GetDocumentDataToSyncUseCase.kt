package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.SyncWarehouseRepository
import com.km.warehouse.domain.usecase.base.UseCase

/**
 * Create by Pustovit Oleksandr on 1/29/2026
 */
class GetDocumentDataToSyncUseCase(private val syncWarehouseRepository: SyncWarehouseRepository) :
    UseCase<Int, Unit>() {
    override suspend fun run(params: Unit): Result<Int> {
        return Result.success(syncWarehouseRepository.getDocumentCountForSync())
    }
}