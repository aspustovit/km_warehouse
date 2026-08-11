package com.km.warehouse.ui.inventory.full

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.km.warehouse.data.converter.InventoryFileTypes
import com.km.warehouse.data.network.entity.ErrorData
import com.km.warehouse.domain.usecase.DeleteInventoryFileUseCase
import com.km.warehouse.domain.usecase.ExportFullInventoryUseCase
import com.km.warehouse.domain.usecase.GetInventoryByFileUseCase
import com.km.warehouse.domain.usecase.GetInventoryFilesUseCase
import com.km.warehouse.domain.usecase.ObserveBarcodeDataUseCase
import com.km.warehouse.domain.usecase.RenameInventoryFileUseCase
import com.km.warehouse.domain.usecase.UpdateFullInventoryModelUseCase
import com.km.warehouse.domain.usecase.UploadInventoryExelFileUseCase
import com.km.warehouse.domain.usecase.base.DataHub
import com.km.warehouse.domain.usecase.inventory.InventoryFileModel
import com.km.warehouse.domain.usecase.inventory.InventoryModel
import com.km.warehouse.ui.move_order.MoveOrderItemViewModel.Companion.NO_SERIAL_NUMBER_CONFLICT
import com.km.warehouse.ui.move_order.MoveOrderItemViewModel.Companion.PARCE_FILE_ERROR
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.toString

/**
 * Create by Pustovit Oleksandr on 08/06/2026
 */
class FullInventoryViewModel(
    private val uploadInventoryExelFileUseCase: UploadInventoryExelFileUseCase,
    private val getInventoryFilesUseCase: GetInventoryFilesUseCase,
    private val getInventoryByFileUseCase: GetInventoryByFileUseCase,
    private val observeBarcodeDataUseCase: ObserveBarcodeDataUseCase,
    private val deleteInventoryFileUseCase: DeleteInventoryFileUseCase,
    private val renameInventoryFileUseCase: RenameInventoryFileUseCase,
    private val updateFullInventoryModelUseCase: UpdateFullInventoryModelUseCase,
    private val exportFullInventoryUseCase: ExportFullInventoryUseCase
) : ViewModel() {
    private var _viewState: MutableStateFlow<FullInventoryState> =
        MutableStateFlow(FullInventoryState(dbFiles = emptyList()))
    val viewState: StateFlow<FullInventoryState> = _viewState
    private var barcodeJob: Job? = null

    fun observeBarcodes() {
        barcodeJob?.cancel()
        barcodeJob = observeBarcodeDataUseCase.observe().onEach { bar ->
            cancelError()
            searchInventoryInFile(bar)

        }.launchIn(viewModelScope)
        observeBarcodeDataUseCase(Unit)
    }

    fun cancelError() {
        _viewState.update { it.copy(error = "", errorData = null) }
    }

    fun showImportExelFileDialog(fileName: String) {
        _viewState.update { it.copy(showLoadFileDialog = true, fileName = fileName) }
    }

    fun loadExelFile(uri: Uri?) {
        if (uri != null) {
            _viewState.update {
                it.copy(
                    showLoadFileDialog = false,
                    parseExelProgress = true,
                    processNewFile = true
                )
            }
            viewModelScope.launch {
                uploadInventoryExelFileUseCase.invoke(Pair(uri, _viewState.value.fileName))
                    .onSuccess {
                        Log.d("EXEL", "Reload $it")
                        if (it)
                            loadSavedFiles(InventoryFileTypes.FULL)
                        else {
                            _viewState.update {
                                _viewState.value.copy(
                                    errorData = ErrorData(
                                        status = PARCE_FILE_ERROR,
                                        message = _viewState.value.fileName,
                                        error = ""
                                    ),
                                    processNewFile = false,
                                    parseExelProgress = false
                                )
                            }
                        }
                    }.onFailure { error ->
                        _viewState.update {
                            _viewState.value.copy(
                                error = error.toString(),
                                processNewFile = false
                            )
                        }
                    }
            }
        }
    }

    fun onInventoryFileSelected(inventoryFile: InventoryFileModel) {
        _viewState.update {
            _viewState.value.copy(
                selectedInventoryFile = inventoryFile
            )
        }
    }

    fun removeFileSelection() {
        _viewState.update {
            _viewState.value.copy(
                selectedInventoryFile = null,
                fileInventoryModels = emptyList(),
                xlsFileUri = null,
                barcode = null,
                showNoInventoryMessage = true
            )
        }
    }

    fun updateInventory(fact: InventoryModel) {
        viewModelScope.launch {
            updateFullInventoryModelUseCase.invoke(fact).onSuccess {
                /*_viewState.update {
                    it.copy(
                        fileInventoryModels = emptyList(),
                        selectedInventory = null,
                        showNoInventoryMessage = false
                    )
                }
                _viewState.value.barcode?.let { b ->
                    searchInventoryInFile(b)
                }*/
                val inventory = ArrayList(viewState.value.fileInventoryModels)
                var factInventory: InventoryModel? = null
                var factIndex: Int = 0
                var modelForRemove: InventoryModel? = null
                inventory.forEachIndexed { index, model ->
                    if(model.id == fact.id) {
                        modelForRemove = model
                        factIndex = index
                        factInventory = fact
                    }
                }
                factInventory?.let {
                    inventory.remove(modelForRemove)
                    inventory.add(factIndex, factInventory)
                    _viewState.update { it.copy(fileInventoryModels = inventory.toList(), selectedInventory = null) }
                }
            }
        }
    }

    fun editInventory(inv: InventoryModel?) {
        _viewState.update { it.copy(selectedInventory = inv) }
    }

    fun loadSavedFiles(fileTypes: InventoryFileTypes) {
        viewModelScope.launch {
            getInventoryFilesUseCase.invoke(fileTypes).onSuccess { dbFiles ->
                _viewState.update {
                    _viewState.value.copy(
                        dbFiles = dbFiles,
                        fileName = "",
                        fileModelForRename = null,
                        fileModelForDelete = null,
                        processNewFile = false,
                        parseExelProgress = false,
                        barcode = null,
                        showNoInventoryMessage = true,
                        fileTypes = fileTypes
                    )
                }
            }.onFailure { ex ->
                _viewState.update {
                    _viewState.value.copy(
                        error = ex.toString()
                    )
                }
            }
        }
    }

    fun searchInventoryInFile(barcode: String) {
        val file = _viewState.value.selectedInventoryFile
        if (file != null) {
            viewModelScope.launch {
                _viewState.update {
                    _viewState.value.copy(
                        barcode = barcode,
                        showNoInventoryMessage = false
                    )
                }
                getInventoryByFileUseCase.invoke(Pair(file.id, barcode)).onSuccess { inventories ->
                    _viewState.update {
                        _viewState.value.copy(
                            fileInventoryModels = inventories.inventory,
                            showNoInventoryMessage = true
                        )
                    }
                }.onFailure { ex ->
                    _viewState.update {
                        _viewState.value.copy(
                            error = ex.toString(),
                            barcode = null
                        )
                    }
                }
            }
        }
    }

    fun setInventoryFileForDelete(inventoryFile: InventoryFileModel) {
        _viewState.update {
            _viewState.value.copy(
                fileModelForDelete = inventoryFile
            )
        }
    }

    fun deleteFile() {
        if (_viewState.value.fileModelForDelete == null)
            return

        viewModelScope.launch {
            deleteInventoryFileUseCase.invoke(_viewState.value.fileModelForDelete!!).onSuccess {
                if (it) {
                    loadSavedFiles(_viewState.value.fileTypes)
                    DataHub.emitSavedBarcodeFileChange(true)
                }
            }.onFailure { ex ->
                _viewState.update {
                    _viewState.value.copy(
                        error = ex.toString()
                    )
                }
            }
        }
    }

    fun startRenameFile(inventoryFile: InventoryFileModel) {
        _viewState.update {
            _viewState.value.copy(
                fileName = inventoryFile.fileName,
                fileModelForRename = inventoryFile
            )
        }
    }

    fun cancelFileDelete() {
        _viewState.update {
            _viewState.value.copy(
                fileModelForDelete = null
            )
        }
    }


    fun finishRename() {
        _viewState.update {
            _viewState.value.copy(
                fileName = "",
                fileModelForRename = null
            )
        }
    }

    fun rename(newName: String) {
        _viewState.value.fileModelForRename?.let {
            viewModelScope.launch {
                renameInventoryFileUseCase.invoke(Pair(it.id, newName)).onSuccess {
                    loadSavedFiles(_viewState.value.fileTypes)
                }.onFailure { ex ->
                    _viewState.update {
                        _viewState.value.copy(
                            error = ex.toString()
                        )
                    }
                }
            }
        }
    }

    fun exportFullInventoryToExel(exelToExportFile: InventoryFileModel? = null) {
        val file = exelToExportFile ?: _viewState.value.selectedInventoryFile
        file?.let {
            viewModelScope.launch {
                exportFullInventoryUseCase.invoke(it.id).onSuccess { f ->
                    _viewState.update {
                        _viewState.value.copy(
                            xlsFileUri = f
                        )
                    }
                }.onFailure { ex ->
                    _viewState.update {
                        _viewState.value.copy(
                            error = ex.toString()
                        )
                    }
                }
            }
        }

    }
}