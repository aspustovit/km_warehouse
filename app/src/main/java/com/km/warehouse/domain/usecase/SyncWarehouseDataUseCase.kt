package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.AuthRepository
import com.km.warehouse.domain.repository.SyncWarehouseRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.model.SyncResultModel

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
class SyncWarehouseDataUseCase(private val syncWarehouseRepository: SyncWarehouseRepository,
                               private val authRepository: AuthRepository) :
    UseCase<SyncResultModel, Unit>() {
    override suspend fun run(params: Unit): Result<SyncResultModel> {
        val syncRes = syncWarehouseRepository.syncWarehouseData()
        if(syncRes.errorData != null && syncRes.errorData.status == 401) {
            val refreshTokens = authRepository.getRefreshLoginToken()
            if(refreshTokens.refreshToken.isBlank()) {
                Result.success(syncRes)
            }
        }
        return Result.success(syncWarehouseRepository.syncWarehouseData())
    }
}