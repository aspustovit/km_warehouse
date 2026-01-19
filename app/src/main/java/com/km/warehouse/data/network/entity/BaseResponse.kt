package com.km.warehouse.data.network.entity

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
data class BaseResponse <T> (
    val data: T?,
    val error: ErrorData
)