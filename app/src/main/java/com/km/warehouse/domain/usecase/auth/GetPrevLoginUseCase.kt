package com.km.warehouse.domain.usecase.auth

import com.km.warehouse.domain.repository.AuthRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.model.PrevAuthModel

/**
 * Create by Pustovit Oleksandr on 1/6/2026
 */
class GetPrevLoginUseCase(private val authRepository: AuthRepository) :
    UseCase<PrevAuthModel, Unit>() {
    override suspend fun run(params: Unit): Result<PrevAuthModel> {
        return Result.success(authRepository.getAuthLogin())
    }
}