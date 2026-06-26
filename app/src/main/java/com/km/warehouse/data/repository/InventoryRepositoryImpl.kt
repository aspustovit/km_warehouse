package com.km.warehouse.data.repository

import android.content.Context
import android.icu.util.Calendar
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.km.warehouse.data.KmWarehouseDatabase
import com.km.warehouse.data.entity.Inventory
import com.km.warehouse.data.entity.InventoryFiles
import com.km.warehouse.data.network.WarehouseApiService
import com.km.warehouse.data.network.entity.ErrorData
import com.km.warehouse.domain.repository.InventoryRepository
import com.km.warehouse.domain.usecase.inventory.InventoryFileModel
import com.km.warehouse.domain.usecase.inventory.InventoryModel
import com.km.warehouse.domain.usecase.inventory.InventorySegmentModel
import com.km.warehouse.ui.utils.ExcelExporter
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import kotlin.collections.forEachIndexed
import kotlin.collections.orEmpty

/**
 * Create by Pustovit Oleksandr on 01/06/2026
 */
class InventoryRepositoryImpl(
    private val warehouseApiService: WarehouseApiService,
    val context: Context,
    val database: KmWarehouseDatabase
) :
    InventoryRepository {
    override suspend fun loadInventoryBySegment(itemSegment: String): InventorySegmentModel {
        val response = warehouseApiService.getInventoryBySegment(itemSegment).execute()
        var errorData: ErrorData? = null
        if (!response.isSuccessful) {
            errorData = parseError(response.errorBody()!!.string())
        }
        val data = response.body()?.data
        val result = ArrayList<InventoryModel>()
        data.orEmpty().forEachIndexed { index, entity -> result.add(entity.toInventoryModel(index)) }
        return InventorySegmentModel(
            inventory = result,
            errorData = errorData
        )
    }

    private fun parseError(errorBody: String): ErrorData? {
        val gson = Gson()
        val error = gson.fromJson(errorBody, ErrorData::class.java)
        Log.e("syncToServerWarehouseData", "$error")
        return error
    }

    override suspend fun loadInventoryByExel(fileUri: Uri, fileName: String): Boolean {
        context.contentResolver.openInputStream(fileUri).use { stream ->
            // Create the workbook object
            try {
                val workbook: Workbook = WorkbookFactory.create(stream)
                // Target the first sheet inside the Excel file
                val sheet = workbook.getSheetAt(0)
                val inventories = ArrayList<Inventory>()
                val fileId = database.inventoryFilesDao().insert(
                    InventoryFiles(
                        createDate = Calendar.getInstance().timeInMillis,
                        fileName = fileName.ifEmpty { fileUri.toString() }
                    )
                )
                Log.i("EXEL", "Start")
                for (row in sheet) {
                    val rowData = StringBuilder()
                    val cells = ArrayList<String>()
                    for (cell in row) {
                        val cellValue = when (cell.cellType) {
                            CellType.STRING -> cell.stringCellValue
                            CellType.NUMERIC -> cell.numericCellValue.toString()
                            CellType.BOOLEAN -> cell.booleanCellValue.toString()
                            CellType.BLANK -> ""
                            else -> "Unknown"
                        }
                        cells.add(cellValue.toString())
                        rowData.append("$cellValue\t")
                    }
                    Log.i("EXEL", "#${row.rowNum}")
                    if(row.rowNum != 0) {
                        inventories.add(
                            Inventory(
                                quantity = cells[4].toDouble(),
                                freeQuantity = cells[5].toDouble(),
                                mfgPartNumber = cells[1],
                                itemSegment = cells[0],
                                itemDescription = cells[3],
                                fileId = fileId.toInt(),
                                inventoryItemId = cells[2]
                            )
                        )
                    }
                }
                Log.i("EXEL", "Parce done")
                inventories.forEach {
                    database.inventoryDao().insert(it)
                    Log.i("EXEL", "$it")
                }

                // Clean up memory resources
                workbook.close()
            } catch (ex: Exception) {
                Log.e("EXEL", "$ex")
                return false
            }
        }
        return true
    }

    override suspend fun geInventoryByFile(
        fileId: Int,
        mfgPartNumber: String
    ): InventorySegmentModel {
        val inventory = database.inventoryDao().getInventoryByFile(fileId, mfgPartNumber)
        val result = ArrayList<InventoryModel>()
        inventory.forEachIndexed { index, entity -> result.add(entity.toInventoryModel(index)) }
        return InventorySegmentModel(inventory = result, errorData = null)
    }

    override suspend fun getInventoryFiles(): List<InventoryFileModel> {
        val files = database.inventoryFilesDao().getInventoryFiles()
        return files.map { it.toInventoryFilesModel() }
    }

    override suspend fun deleteInventoryFile(fileId: Int): Boolean {
        database.inventoryDao().deleteInventoryByFile(fileId)
        val deleteCount = database.inventoryFilesDao().deleteFile(fileId)
        return deleteCount != 0
    }

    override suspend fun renameFile(fileId: Int, newName: String): Boolean {
        return database.inventoryFilesDao().renameFile(fileId, newName) != 0
    }

    override suspend fun updateInventoryModelInFile(inventoryModel: InventoryModel): Boolean {
        val id = database.inventoryDao().update(inventoryModel.toInventory())
        return id != 0
    }

    override suspend fun exportAllInventoryModelFileData(fileId: Int): Uri? {
        val result = ArrayList<InventoryModel>()
        database.inventoryDao().exportInventoryByFile(fileId).forEachIndexed { index, inventory -> result.add(inventory.toInventoryModel(index)) }
        val file = database.inventoryFilesDao().getInventoryFileById(fileId)
        val fileUri = ExcelExporter.export(
            context,
            result,
            file.fileName.replace(" ","_")
        )
        return fileUri
    }
}