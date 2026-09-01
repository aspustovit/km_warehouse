package com.km.warehouse.data.repository

import android.content.Context
import android.icu.util.Calendar
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.km.warehouse.data.KmWarehouseDatabase
import com.km.warehouse.data.converter.InventoryFileTypes
import com.km.warehouse.data.converter.PidzapasTypes
import com.km.warehouse.data.entity.Inventory
import com.km.warehouse.data.entity.InventoryFiles
import com.km.warehouse.data.network.WarehouseApiService
import com.km.warehouse.data.network.entity.ErrorData
import com.km.warehouse.domain.repository.InventoryRepository
import com.km.warehouse.domain.usecase.inventory.InventoryFileModel
import com.km.warehouse.domain.usecase.inventory.InventoryModel
import com.km.warehouse.domain.usecase.inventory.InventorySegmentModel
import com.km.warehouse.domain.usecase.inventory.UserWarehouseModel
import com.km.warehouse.ui.utils.ExcelExporter
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import kotlin.collections.forEachIndexed
import kotlin.collections.orEmpty
import kotlin.text.ifEmpty

/**
 * Create by Pustovit Oleksandr on 01/06/2026
 */
class InventoryRepositoryImpl(
    private val warehouseApiService: WarehouseApiService,
    val context: Context,
    val database: KmWarehouseDatabase
) :
    InventoryRepository {
    override suspend fun loadInventoryBySegment(segment: Pair<String, Int>): InventorySegmentModel {
        val itemSegment = segment.first
        Log.v("loadInventoryBySegment", "Start load")
        val lastIndex = segment.second
        val response = warehouseApiService.getInventoryBySegment(itemSegment).execute()
        var errorData: ErrorData? = null
        if (!response.isSuccessful) {
            errorData = parseError(response.errorBody()!!.string())
        }
        val data = response.body()?.data
        val partInventories = ArrayList<InventoryModel>()
        data.orEmpty()
            .forEachIndexed { index, entity -> partInventories.add(entity.toInventoryModel(index)) }
        if (partInventories.isEmpty()) {
            val mfrResponse = warehouseApiService.getInventoryByMfrPartNumber(itemSegment).execute()
            if (!mfrResponse.isSuccessful) {
                errorData = parseError(mfrResponse.errorBody()!!.string())
            }
            val mfrData = mfrResponse.body()?.data
            mfrData.orEmpty()
                .forEachIndexed { index, entity -> partInventories.add(entity.toInventoryModel(lastIndex + index)) }
        }
        Log.e("loadInventoryBySegment", "End load")

        val userWarehouses = database.userWarehouseDao().getUserWarehouse()
        val result = if(userWarehouses.isEmpty()){
            partInventories
        } else {
            val res = ArrayList<InventoryModel>()
            partInventories.forEach { part ->
                userWarehouses.forEach { warehouse ->
                    if(warehouse.warehouseName == part.subInventoryCode){
                        res.add(part)
                    }
                }
            }
            res
        }
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
                        fileName = fileName.ifEmpty { fileUri.toString() },
                        fileType = InventoryFileTypes.FULL
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
                            CellType.BLANK -> {
                                ""
                            }

                            else -> "Unknown"
                        }
                        cells.add(cellValue.toString())
                        rowData.append("$cellValue\t")
                    }
                    Log.i("EXEL", "#${row.rowNum}")
                    if (row.rowNum != 0) {
                        if (cells.size == 5)
                            cells.add(1, "")
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
        var inventory = if (mfgPartNumber.isNotEmpty()) {
            val barcodes = mfgPartNumber.split("/")
            val res = ArrayList<Inventory>()
            Log.e("BARCODE_SCAN", "$barcodes")
            val filteredBarcodes = ArrayList<String>()
            barcodes.forEachIndexed { index, b ->
                if (b == "G" || b == "T") {
                    if (index != 0) {
                        val prevPart = filteredBarcodes[index - 1]
                        filteredBarcodes.remove(prevPart)
                        filteredBarcodes.add("$prevPart/$b")
                    }
                } else {
                    filteredBarcodes.add(b)
                }
            }
            filteredBarcodes.forEach { b ->
                if (b.length > 1)
                    res.addAll(database.inventoryDao().getInventoryByItemSegment(fileId, b))
            }
            filteredBarcodes.forEach { b ->
                if (b.length > 1)
                    res.addAll(database.inventoryDao().getInventoryByFile(fileId, b))
            }
            filteredBarcodes.forEach { b ->
                if (b.length > 1)
                    res.addAll(database.inventoryDao().getInventoryByInventoryItem(fileId, b))
            }
            res
        } else {
            database.inventoryDao().getEmptyBarcodes(fileId)
        }
        if (inventory.isEmpty())
            inventory = database.inventoryDao().getInventoryByItemId(fileId, mfgPartNumber)
        val result = ArrayList<InventoryModel>()
        inventory.forEachIndexed { index, entity -> result.add(entity.toInventoryModel(index)) }
        val distinctList = result.distinctBy { it.inventoryItemId }
        return InventorySegmentModel(inventory = distinctList, errorData = null)
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
        database.inventoryDao().exportInventoryByFile(fileId)
            .forEachIndexed { index, inventory -> result.add(inventory.toInventoryModel(index)) }
        val file = database.inventoryFilesDao().getInventoryFileById(fileId)
        val fileUri = ExcelExporter.export(
            context,
            result,
            file.fileName.replace(" ", "_")
        )
        return fileUri
    }

    override suspend fun savePartInventoryToDB(params: Pair<String, List<InventoryModel>>): Int {
        val fileId = database.inventoryFilesDao().insert(
            InventoryFiles(
                createDate = Calendar.getInstance().timeInMillis,
                fileName = params.first,
                fileType = InventoryFileTypes.PART,
                comments = "Часткова інвентаризація по номеру ${params.first}. Всього одиниць - ${params.second.size}"
            )
        )
        val inventories = params.second.map { it.toInventory(fileId) }
        try {
            inventories.forEach {
                Log.i("savePartInventoryToDB", "$it")
                database.inventoryDao().insert(it)
            }
        } catch (ex: Exception) {
            Log.e("savePartInventoryToDB", "$ex")
        }

        return fileId.toInt()
    }

    override suspend fun getPartInventoryFromDB(): List<InventoryFileModel> {
        return database.inventoryFilesDao().getInventoryFiles(InventoryFileTypes.PART)
            .map { it.toInventoryFilesModel() }
    }

    override suspend fun getPartInventory(partFileId: Int): List<InventoryModel> {
        val result = ArrayList<InventoryModel>()
        database.inventoryDao().getPartInventoryByFile(partFileId)
            .forEachIndexed { index, inventory ->
                result.add(inventory.toInventoryModel(index))
            }
        return result
    }

    override suspend fun getUserWarehouses(): List<UserWarehouseModel> {
        val saved = database.userWarehouseDao().getUserWarehouse()
        val userWarehouseModels = ArrayList<UserWarehouseModel>()
        PidzapasTypes.entries.forEach {
            userWarehouseModels.add(it.toUserWarehouseModel(isSelected = saved.find { s -> s.warehouseName == it.warehouseName } != null))
        }
        return userWarehouseModels
    }

    override suspend fun saveUserWarehouses(userWarehouses: List<UserWarehouseModel>): Boolean {
        database.userWarehouseDao().deleteWarehouseSettings()
        val selectedWarehouses = userWarehouses.filter { it.isSelected }
        var insertId = 0L
        selectedWarehouses.map { it.toUserWarehouse() }.forEach {
            insertId = database.userWarehouseDao().insert(it)
        }
        return insertId != 0L
    }
}