package com.km.warehouse.domain.repository

import com.km.warehouse.data.network.auth.AuthRequest
import com.km.warehouse.domain.usecase.model.LoginModel
import com.km.warehouse.domain.usecase.model.PrevAuthModel

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
interface AuthRepository {
      suspend fun login(auth: AuthRequest): LoginModel

      suspend fun getRefreshLoginToken(): PrevAuthModel
}