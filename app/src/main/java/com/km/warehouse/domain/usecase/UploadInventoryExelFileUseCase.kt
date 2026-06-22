package com.km.warehouse.domain.usecase

import android.net.Uri
import com.km.warehouse.domain.repository.InventoryRepository
import com.km.warehouse.domain.usecase.base.UseCase

/**
 * Create by Pustovit Oleksandr on 08/06/2026
 */
class UploadInventoryExelFileUseCase(private val inventoryRepository: InventoryRepository) :
    UseCase<Boolean, Pair<Uri, String>>() {
    override suspend fun run(params: Pair<Uri, String>): Result<Boolean> {
        return Result.success(inventoryRepository.loadInventoryByExel(params.first, params.second))
    }
}