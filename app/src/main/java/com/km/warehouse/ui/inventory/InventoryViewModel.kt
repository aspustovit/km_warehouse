package com.km.warehouse.ui.inventory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.km.warehouse.domain.usecase.GetInventorySegmentUseCase
import com.km.warehouse.domain.usecase.GetPartFilesInventoryFromDBUseCase
import com.km.warehouse.domain.usecase.GetPartInventoryByFileIdUseCase
import com.km.warehouse.domain.usecase.ObserveBarcodeDataUseCase
import com.km.warehouse.domain.usecase.ObservePartFileChangeUseCase
import com.km.warehouse.domain.usecase.SavePartInventoryToDBUseCase
import com.km.warehouse.domain.usecase.UpdateFullInventoryModelUseCase
import com.km.warehouse.domain.usecase.UploadInventoryExelFileUseCase
import com.km.warehouse.domain.usecase.base.DataHub
import com.km.warehouse.domain.usecase.inventory.InventoryModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Create by Pustovit Oleksandr on 01/06/2026
 */
class InventoryViewModel(
    private val getInventorySegmentUseCase: GetInventorySegmentUseCase,
    private val observeBarcodeDataUseCase: ObserveBarcodeDataUseCase,
    private val uploadInventoryExelFileUseCase: UploadInventoryExelFileUseCase,
    private val savePartInventoryToDBUseCase: SavePartInventoryToDBUseCase,
    private val getPartFilesInventoryFromDBUseCase: GetPartFilesInventoryFromDBUseCase,
    private val getPartInventoryByFileIdUseCase: GetPartInventoryByFileIdUseCase,
    private val updateFullInventoryModelUseCase: UpdateFullInventoryModelUseCase,
    private val observePartFileChangeUseCase: ObservePartFileChangeUseCase
) :
    ViewModel() {

    private var _viewState: MutableStateFlow<InventoryState> =
        MutableStateFlow(InventoryState(inventory = emptyList()))
    val viewState: StateFlow<InventoryState> = _viewState
    private var barcodeJob: Job? = null
    private var partFileJob: Job? = null

    fun observeBarcodes() {
        barcodeJob?.cancel()
        partFileJob?.cancel()
        Log.e("onKeyDown_SCAN_3", "LAUNCH")
        barcodeJob = observeBarcodeDataUseCase.observe().onEach { bar ->
            cancelError()
            Log.e("onKeyDown_SCAN_5", bar)
            Log.e("onKeyDown_SCAN_5", "${_viewState.value.inventoryListLoading}")
            if (!_viewState.value.inventoryListLoading) {
                loadInventorySegment(bar)
            }
        }.launchIn(viewModelScope)
        observeBarcodeDataUseCase(Unit)

        partFileJob = observePartFileChangeUseCase.observe().onEach {
            loadPartInventoryFromDB()
        }.launchIn(viewModelScope)
        observePartFileChangeUseCase(Unit)
    }

    fun loadInventorySegment(segmentId: String) {
        if (_viewState.value.showFullInventoryScreen)
            return

        _viewState.update {
            _viewState.value.copy(
                inventoryListLoading = true,
                inventoryBarcode = segmentId
            )
        }
        viewModelScope.launch {
            Log.d("InventorySegment", segmentId)
            val lastPrevItemId = if(_viewState.value.inventory.isEmpty()) 0 else _viewState.value.inventory.last().id+1
            getInventorySegmentUseCase.invoke(Pair(segmentId, lastPrevItemId)).onSuccess { inv ->
                val prevInventory = _viewState.value.inventory
                val newInventory = ArrayList(inv.inventory)
                newInventory.addAll(prevInventory)
                _viewState.update {
                    _viewState.value.copy(
                        inventory = newInventory.toList(),
                        errorData = inv.errorData,
                        inventoryListLoading = false
                    )
                }
            }.onFailure {
                _viewState.update {
                    _viewState.value.copy(
                        error = it.toString(),
                        inventoryListLoading = false
                    )
                }
            }
        }
    }

    fun cancelError() {
        _viewState.update { it.copy(error = "", errorData = null) }
    }

    fun editInventory(inv: InventoryModel?) {
        _viewState.update { it.copy(selectedInventory = inv) }
    }

    fun updateInventory(fact: InventoryModel) {

        val inventory = ArrayList(viewState.value.inventory)
        var factInventory: InventoryModel? = null
        var factIndex: Int = 0
        var modelForRemove: InventoryModel? = null
        inventory.forEachIndexed { index, model ->
            if (model.id == fact.id) {
                modelForRemove = model
                factIndex = index
                factInventory = fact
            }
        }
        factInventory?.let {
            inventory.remove(modelForRemove)
            inventory.add(factIndex, factInventory)
            _viewState.update { it.copy(inventory = inventory.toList(), selectedInventory = null) }
        }
        if(_viewState.value.partFileId != null) {
            viewModelScope.launch {
                updateFullInventoryModelUseCase.invoke(fact)
            }
        }
    }

    fun showExitWarning() {
        _viewState.update { it.copy(showExitDialog = true) }
    }

    fun cancelExitWarning() {
        _viewState.update { it.copy(showExitDialog = false) }
    }

    fun showImportExelFileDialog() {
        _viewState.update { it.copy(showLoadFileDialog = true) }
    }

    fun showFullInventoryScreen() {
        _viewState.update { it.copy(showFullInventoryScreen = true) }
    }

    fun showPartInventoryScreen() {
        _viewState.update { it.copy(showPartInventoryScreen = true) }
    }

    fun cancelFullInventoryScreen() {
        _viewState.update {
            it.copy(
                showFullInventoryScreen = false,
                inventoryBarcode = "",
                showPartInventoryScreen = false
            )
        }
    }

    fun savePartInventoryToDB() {
        viewModelScope.launch {
            savePartInventoryToDBUseCase.invoke(
                Pair(
                    _viewState.value.inventoryBarcode,
                    _viewState.value.inventory
                )
            ).onSuccess {
                loadPartInventoryFromDB()
            }.onFailure { ex ->
                _viewState.update { it.copy(error = ex.toString()) }
            }
        }
    }

    fun loadPartInventoryFromDB() {
        viewModelScope.launch {
            getPartFilesInventoryFromDBUseCase.invoke(Unit).onSuccess { partFiles ->
                _viewState.update {
                    it.copy(
                        savedPartInventoryFiles = partFiles
                    )
                }
            }.onFailure { ex ->
                _viewState.update { it.copy(error = ex.toString()) }
            }
        }
    }

    fun postPartNumberFile(partFileId: Int) {
        viewModelScope.launch {
            getPartInventoryByFileIdUseCase.invoke(partFileId).onSuccess { inv ->
                _viewState.update {
                    _viewState.value.copy(
                        inventory = inv,
                        errorData = null,
                        inventoryListLoading = false,
                        showPartInventoryScreen = false,
                        partFileId = partFileId
                    )
                }
            }.onFailure { ex ->
                _viewState.update { it.copy(error = ex.toString()) }
            }
        }
    }

    fun returnToDefState() {
        _viewState.update {
            _viewState.value.copy(
                inventory = emptyList(),
                errorData = null,
                inventoryListLoading = false,
                showPartInventoryScreen = false,
                partFileId = null,
                inventoryBarcode = ""
            )
        }
    }
}