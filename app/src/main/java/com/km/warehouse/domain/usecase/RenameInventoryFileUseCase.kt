package com.km.warehouse.domain.usecase

import com.km.warehouse.domain.repository.InventoryRepository
import com.km.warehouse.domain.usecase.base.UseCase

/**
 * Create by Pustovit Oleksandr on 17/06/2026
 */
class RenameInventoryFileUseCase(private val inventoryRepository: InventoryRepository) :
    UseCase<Boolean, Pair<Int, String>>() {
    override suspend fun run(params: Pair<Int, String>): Result<Boolean> {
        return Result.success(inventoryRepository.renameFile(params.first, params.second))
    }
}