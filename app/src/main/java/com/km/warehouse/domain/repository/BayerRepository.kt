package com.km.warehouse.domain.repository

import com.km.warehouse.data.entity.Bayer

/**
 * Create by Pustovit Oleksandr on 9/23/2025
 */
interface BayerRepository {
    suspend fun getAllBayer(): List<Bayer>
}