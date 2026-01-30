package com.km.warehouse.domain.usecase.model

import com.km.warehouse.data.network.entity.ErrorData

/**
 * Create by Pustovit Oleksandr on 1/6/2026
 */
data class LoginModel(val isLoggedIn: Boolean, val error: ErrorData?)