package com.km.warehouse.domain.repository

import android.net.Uri
import com.km.warehouse.domain.usecase.inventory.InventoryFileModel
import com.km.warehouse.domain.usecase.inventory.InventoryModel
import com.km.warehouse.domain.usecase.inventory.InventorySegmentModel
import com.km.warehouse.domain.usecase.inventory.UserWarehouseModel

/**
 * Create by Pustovit Oleksandr on 01/06/2026
 */
interface InventoryRepository {
    suspend fun loadInventoryBySegment(segment: Pair<String, Int>): InventorySegmentModel

    suspend fun loadInventoryByExel(fileUri: Uri, fileName: String): Boolean

    suspend fun geInventoryByFile(fileId: Int, mfgPartNumber: String): InventorySegmentModel

    suspend fun getInventoryFiles(): List<InventoryFileModel>

    suspend fun deleteInventoryFile(fileId: Int): Boolean

    suspend fun renameFile(fileId: Int, newName: String): Boolean

    suspend fun updateInventoryModelInFile(inventoryModel: InventoryModel): Boolean

    suspend fun exportAllInventoryModelFileData(fileId: Int): Uri?

    suspend fun savePartInventoryToDB(params: Pair<String, List<InventoryModel>>): Int

    suspend fun getPartInventoryFromDB(): List<InventoryFileModel>

    suspend fun getPartInventory(partFileId: Int): List<InventoryModel>

    suspend fun getUserWarehouses(): List<UserWarehouseModel>

    suspend fun saveUserWarehouses(userWarehouses: List<UserWarehouseModel>): Boolean
}