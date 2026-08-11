package com.km.warehouse.ui.inventory.full

import android.net.Uri
import com.km.warehouse.data.converter.InventoryFileTypes
import com.km.warehouse.data.network.entity.ErrorData
import com.km.warehouse.domain.usecase.inventory.InventoryFileModel
import com.km.warehouse.domain.usecase.inventory.InventoryModel

/**
 * Create by Pustovit Oleksandr on 08/06/2026
 */
data class FullInventoryState(
    val dbFiles: List<InventoryFileModel>,
    val errorData: ErrorData? = null,
    val error: String? = null,
    val showLoadFileDialog: Boolean = false,
    val processNewFile: Boolean = false,
    val parseExelProgress: Boolean = false,
    val selectedInventoryFile: InventoryFileModel? = null,
    val fileInventoryModels: List<InventoryModel> = emptyList(),
    val fileModelForRename: InventoryFileModel? = null,
    val fileModelForDelete: InventoryFileModel? = null,
    val fileName: String = "",
    val selectedInventory: InventoryModel? = null,
    val xlsFileUri: Uri? = null,
    val barcode: String? = null,
    val showNoInventoryMessage: Boolean = true,
    val fileTypes: InventoryFileTypes = InventoryFileTypes.FULL
)
