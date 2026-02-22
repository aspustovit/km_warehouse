package com.km.warehouse.domain.usecase

import com.km.warehouse.data.network.entity.ErrorData
import com.km.warehouse.domain.repository.LocalWarehouseRepository
import com.km.warehouse.domain.usecase.base.UseCase

/**
 * Created by aspus on 18.02.2026.
 */
class CheckInputSerialsUseCase(private val localWarehouseRepository: LocalWarehouseRepository) :
    UseCase<ErrorData, String>()  {
    override suspend fun run(params: String): Result<ErrorData> {
        return Result.success(localWarehouseRepository.checkSerialAlreadyEnter(params))
    }

}