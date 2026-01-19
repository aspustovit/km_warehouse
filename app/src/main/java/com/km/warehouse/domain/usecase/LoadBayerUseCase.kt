package com.km.warehouse.domain.usecase

import com.km.warehouse.data.entity.Bayer
import com.km.warehouse.domain.repository.LocalWarehouseRepository
import com.km.warehouse.domain.usecase.base.UseCase

/**
 * Create by Pustovit Oleksandr on 9/24/2025
 */
class LoadBayerUseCase(private val bayerRepository: LocalWarehouseRepository) :
    UseCase<List<Bayer>, Unit>()  {
    override suspend fun run(params: Unit): Result<List<Bayer>> {
        return Result.success(bayerRepository.getAllBayer())
    }
}