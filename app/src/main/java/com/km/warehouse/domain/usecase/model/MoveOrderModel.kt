package com.km.warehouse.domain.usecase.model

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
data class MoveOrderModel(
    val id: Int = 0,
    val description: String,
    val creationDate: String,
    val moveDate: String,
    val number: String,
    val bayerId: Int,
    val status: String,
    val bayerName: String,
    val isComplete: Boolean
)