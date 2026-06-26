package com.km.warehouse.ui.inventory

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.km.warehouse.domain.usecase.GetInventorySegmentUseCase
import com.km.warehouse.domain.usecase.ObserveBarcodeDataUseCase
import com.km.warehouse.domain.usecase.UploadInventoryExelFileUseCase
import com.km.warehouse.domain.usecase.inventory.InventoryModel
import com.km.warehouse.domain.usecase.inventory.UpdateInventoryModel
import com.km.warehouse.ui.move_order.MoveOrderState
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
    private val uploadInventoryExelFileUseCase: UploadInventoryExelFileUseCase
) :
    ViewModel() {

    private var _viewState: MutableStateFlow<InventoryState> =
        MutableStateFlow(InventoryState(inventory = emptyList()))
    val viewState: StateFlow<InventoryState> = _viewState
    private var barcodeJob: Job? = null

    fun observeBarcodes() {
        barcodeJob?.cancel()
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
    }

    fun loadInventorySegment(segmentId: String) {
        if(_viewState.value.showFullInventoryScreen)
            return

        _viewState.update {
            _viewState.value.copy(
                inventoryListLoading = true,
                inventoryBarcode = segmentId
            )
        }
        viewModelScope.launch {
            Log.d("InventorySegment", segmentId)
            getInventorySegmentUseCase.invoke(segmentId).onSuccess { inv ->
                _viewState.update {
                    _viewState.value.copy(
                        inventory = inv.inventory,
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
            if(model.id == fact.id) {
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

    fun cancelFullInventoryScreen() {
        _viewState.update { it.copy(showFullInventoryScreen = false, inventoryBarcode = "") }
    }
}