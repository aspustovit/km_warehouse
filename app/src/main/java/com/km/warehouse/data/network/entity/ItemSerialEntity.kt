package com.km.warehouse.data.network.entity

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
data class ItemSerialEntity(
    override val id: Int,
    val serial: String,
    val moveOrderItemId: Int
) : ResponseEntity(id)
