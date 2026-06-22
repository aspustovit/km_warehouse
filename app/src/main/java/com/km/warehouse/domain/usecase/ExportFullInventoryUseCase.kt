package com.km.warehouse.domain.usecase

import android.net.Uri
import com.km.warehouse.domain.repository.InventoryRepository
import com.km.warehouse.domain.usecase.base.UseCase

/**
 * Create by Pustovit Oleksandr on 19/06/2026
 */
class ExportFullInventoryUseCase(private val inventoryRepository: InventoryRepository) :
    UseCase<Uri, Int>() {
    override suspend fun run(params: Int): Result<Uri> {
        val uri = inventoryRepository.exportAllInventoryModelFileData(params)
        return if(uri != null)
            Result.success(uri)
        else
            Result.failure(Exception("Помилка генерації файлу *.xls"))
    }
}