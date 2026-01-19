package com.km.warehouse.ui.auth

import com.km.warehouse.domain.usecase.model.LoginModel
import com.km.warehouse.domain.usecase.model.PrevAuthModel

/**
 * Create by Pustovit Oleksandr on 1/6/2026
 */
data class AuthState(
    val prevLogin: PrevAuthModel?,
    val loginModel: LoginModel,
    val isLoading: Boolean = false
)
