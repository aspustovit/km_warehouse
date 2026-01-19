package com.km.warehouse.domain.usecase.auth

import com.km.warehouse.data.network.auth.AuthRequest
import com.km.warehouse.domain.repository.AuthRepository
import com.km.warehouse.domain.usecase.base.UseCase
import com.km.warehouse.domain.usecase.model.LoginModel

/**
 * Create by Pustovit Oleksandr on 1/6/2026
 */
class LoginUseCase(private val authRepository: AuthRepository) :
    UseCase<LoginModel, AuthRequest>() {
    override suspend fun run(params: AuthRequest): Result<LoginModel> {
        return Result.success(authRepository.login(params))
    }
}