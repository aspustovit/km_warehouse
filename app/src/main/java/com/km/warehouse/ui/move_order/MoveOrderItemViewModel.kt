package com.km.warehouse.ui.move_order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.km.warehouse.data.network.entity.ErrorData
import com.km.warehouse.domain.usecase.LoadMoveOrdersUseCase
import com.km.warehouse.domain.usecase.ObserveBarcodeDataUseCase
import com.km.warehouse.domain.usecase.SaveSerialToDBUseCase
import com.km.warehouse.domain.usecase.SyncToServerSerialsUseCase
import com.km.warehouse.domain.usecase.auth.GetPrevLoginUseCase
import com.km.warehouse.domain.usecase.model.ItemSerialModel
import com.km.warehouse.ui.scan_to_file.BarcodeReadState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Create by Pustovit Oleksandr on 9/24/2025
 */
class MoveOrderItemViewModel(
    private val loadMoveOrdersUseCase: LoadMoveOrdersUseCase,
    private val observeBarcodeDataUseCase: ObserveBarcodeDataUseCase,
    private val saveSerialToDBUseCase: SaveSerialToDBUseCase,
    private val syncToServerSerialsUseCase: SyncToServerSerialsUseCase,
    private val getPrevLoginUseCase: GetPrevLoginUseCase
) : ViewModel() {
    companion object {
        val SERIAL_NUMBER_NOT_FOUND = 1024
    }

    private var _viewState: MutableStateFlow<MoveOrderState> = MutableStateFlow(
        MoveOrderState(bayers = emptyList())
    )
    val viewState: StateFlow<MoveOrderState> = _viewState

    // Barcode scan observer
    private var _barcodeState: MutableStateFlow<BarcodeReadState> =
        MutableStateFlow(BarcodeReadState(lastBarcode = ""))
    val barcodeState: StateFlow<BarcodeReadState> = _barcodeState
    private var barcode: String = ""
    private var barcodeJob: Job? = null

    fun observeBarcodes() {
        barcodeJob?.cancel()
        Log.e("onKeyDown_SCAN_3", "LAUNCH")
        barcodeJob = observeBarcodeDataUseCase.observe().onEach { bar ->
            Log.e("onKeyDown_SCAN_4", bar)
            searchBarcodeInOrderItems(bar)
        }.launchIn(viewModelScope)
        observeBarcodeDataUseCase(Unit)
    }

    fun searchBarcodeInOrderItems(barcode: String) {
        val orders = viewState.value.moveOrders
        val serials = viewState.value.itemSerials.toMutableList()
        orders.apply {
            forEach {
                it.value.forEach { item ->
                    item.moveOrderItemsModels.forEach { serial ->
                        serial.mfgPartNumExp?.apply {
                            val barcodePatterns = this.split("/")
                            barcodePatterns.forEach { pattern ->
                                if (barcode.contains(pattern)) {
                                    val itemSerial = ItemSerialModel(
                                        serial = barcode,
                                        moveOrderItemId = serial.id
                                    )
                                    serials.add(itemSerial)
                                    saveItemSerialToDb(itemSerial)
                                    Log.i("SERIALS_", "$itemSerial")
                                    Log.d("SERIALS_S", "$serials")
                                    _viewState.update { _viewState.value.copy(itemSerials = serials.toList()) }
                                    return
                                }
                            }
                        }
                    }
                }
            }
            _viewState.update {
                _viewState.value.copy(
                    errorData = ErrorData(
                        status = SERIAL_NUMBER_NOT_FOUND,
                        message = barcode,
                        error = "Barcode not found!"
                    )
                )
            }
        }
    }

    fun saveItemSerialToDb(itemSerialModel: ItemSerialModel) {
        viewModelScope.launch {
            saveSerialToDBUseCase.invoke(itemSerialModel).onSuccess {
                Log.d("saveItemSerialToDb", "SAVED")
            }.onFailure {
                _viewState.update { it.copy(error = "$it") }
            }
        }
    }

    fun sync() {
        viewModelScope.launch {
            syncToServerSerialsUseCase.invoke(Unit).onSuccess {
                if (!it.isSyncSuccess) {
                    _viewState.update { state -> state.copy(errorData = it.errorData) }
                    if (it.errorData?.status == 401) {
                        updateTokens()
                    }
                } else {

                }
            }
        }
    }

    fun updateTokens() {
        viewModelScope.launch {
            getPrevLoginUseCase.invoke(Unit).onSuccess {
                Log.d("syncToServerWarehouseData", "$it")
            }
        }
    }

    fun loadMoveOrders() {
        viewModelScope.launch {
            loadMoveOrdersUseCase.invoke(Unit).onSuccess { result ->
                _viewState.update { it.copy(moveOrders = result, errorData = null) }
            }.onFailure {
                _viewState.update { it.copy(error = "$it") }
            }
        }
    }

    fun cancelError() {
        _viewState.update { it.copy(error = "", errorData = null) }
    }
}