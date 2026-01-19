package com.km.warehouse.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.km.warehouse.domain.usecase.ObserveBarcodeDataUseCase
import com.km.warehouse.domain.usecase.SyncWarehouseDataUseCase
import com.km.warehouse.domain.usecase.base.DataHub
import com.km.warehouse.ui.scan_to_file.BarcodeReadState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Create by Pustovit Oleksandr on 9/26/2025
 */
class SharedViewModel(
    private val observeBarcodeDataUseCase: ObserveBarcodeDataUseCase
) : ViewModel() {
    private var _barcodeState: MutableStateFlow<BarcodeReadState> =
        MutableStateFlow(BarcodeReadState(lastBarcode = ""))
    val viewState: StateFlow<BarcodeReadState> = _barcodeState

    private var barcode: String = ""

    private var barcodeJob: Job? = null
    fun observeBarcodes() {
        barcodeJob?.cancel()

        barcodeJob = observeBarcodeDataUseCase.observe().onEach { bar ->
            Log.e("onKeyDown_SCAN_3", bar)
            _barcodeState.update {
                it.copy(
                    lastBarcode = bar,
                    barcodeData = "${it.barcodeData}$bar"
                )
            }
        }.launchIn(viewModelScope)
        observeBarcodeDataUseCase(Unit)
    }

    fun onBarcodeScan(scanChar: Char) {
        barcode += scanChar
    }

    fun postBarcode() {
        Log.i("onKeyDown_SCAN", "barcode = $barcode")
        viewModelScope.launch {
            DataHub.emitBarcodeData(barcode)
        }
        barcode = ""
    }
}