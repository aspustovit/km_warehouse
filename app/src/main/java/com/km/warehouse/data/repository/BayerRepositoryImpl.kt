package com.km.warehouse.data.repository

import com.km.warehouse.data.KmWarehouseDatabase
import com.km.warehouse.data.entity.Bayer
import com.km.warehouse.domain.repository.BayerRepository

/**
 * Create by Pustovit Oleksandr on 9/23/2025
 */
class BayerRepositoryImpl(val database: KmWarehouseDatabase) : BayerRepository {
    override suspend fun getAllBayer(): List<Bayer> {
        TODO("Not yet implemented")
    }
}