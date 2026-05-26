package com.km.warehouse.domain.usecase.auth

import com.km.warehouse.domain.repository.AuthRepository
import com.km.warehouse.domain.usecase.base.UseCase

/**
 * Create by Pustovit Oleksandr on 25/05/2026
 */
class LogoutUseCase(private val authRepository: AuthRepository) :
    UseCase<Unit, Unit>() {
    override suspend fun run(params: Unit): Result<Unit> {
        return Result.success(authRepository.logout())
    }
}