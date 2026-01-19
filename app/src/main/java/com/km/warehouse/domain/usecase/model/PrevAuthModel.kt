package com.km.warehouse.domain.usecase.model

/**
 * Create by Pustovit Oleksandr on 1/6/2026
 */
data class PrevAuthModel(
    val token: String,
    val refreshToken: String,
    val userName: String
)
