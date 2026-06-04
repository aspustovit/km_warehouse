package com.km.warehouse.data.repository

import android.util.Log
import com.google.gson.Gson
import com.km.warehouse.data.network.WarehouseApiService
import com.km.warehouse.data.network.entity.ErrorData
import com.km.warehouse.domain.repository.InventoryRepository
import com.km.warehouse.domain.usecase.inventory.InventorySegmentModel

/**
 * Create by Pustovit Oleksandr on 01/06/2026
 */
class InventoryRepositoryImpl(private val warehouseApiService: WarehouseApiService) :
    InventoryRepository {
    override suspend fun loadInventoryBySegment(itemSegment: String): InventorySegmentModel {
        val response = warehouseApiService.getInventoryBySegment(itemSegment).execute()
        var errorData: ErrorData? = null
        if (!response.isSuccessful) {
            errorData = parseError(response.errorBody()!!.string())
        }
        val data = response.body()?.data
        return InventorySegmentModel(
            inventory = data.orEmpty().map { it.toInventoryModel() },
            errorData = errorData
        )
    }

    private fun parseError(errorBody: String): ErrorData? {
        val gson = Gson()
        val error = gson.fromJson(errorBody, ErrorData::class.java)
        Log.e("syncToServerWarehouseData", "$error")
        return error
    }
}