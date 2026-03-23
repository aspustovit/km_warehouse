package com.km.warehouse.ui.move_order

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.km.warehouse.data.network.entity.ErrorData
import com.km.warehouse.domain.usecase.CheckInputSerialsUseCase
import com.km.warehouse.domain.usecase.DeleteSerialNumberUseCase
import com.km.warehouse.domain.usecase.GetItemSerialFromDBUseCase
import com.km.warehouse.domain.usecase.LoadMoveOrdersUseCase
import com.km.warehouse.domain.usecase.ObserveBarcodeDataUseCase
import com.km.warehouse.domain.usecase.SaveSerialToDBUseCase
import com.km.warehouse.domain.usecase.SetNoSerialsUseCase
import com.km.warehouse.domain.usecase.SetQuantityGivenUseCase
import com.km.warehouse.domain.usecase.SyncToServerSerialsUseCase
import com.km.warehouse.domain.usecase.auth.GetPrevLoginUseCase
import com.km.warehouse.domain.usecase.model.ItemSerialModel
import com.km.warehouse.domain.usecase.model.MoveOrderItemsModel
import com.km.warehouse.domain.usecase.model.MoveOrderModel
import com.km.warehouse.domain.usecase.model.OrderModel
import com.km.warehouse.domain.usecase.model.QuantityGivenModel
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
    private val getPrevLoginUseCase: GetPrevLoginUseCase,
    private val getItemSerialFromDBUseCase: GetItemSerialFromDBUseCase,
    private val deleteSerialNumberUseCase: DeleteSerialNumberUseCase,
    private val setQuantityGivenUseCase: SetQuantityGivenUseCase,
    private val setNoSerialsUseCase: SetNoSerialsUseCase,
    private val checkInputSerialsUseCase: CheckInputSerialsUseCase
) : ViewModel() {
    companion object {
        val SERIAL_NUMBER_NOT_FOUND = 1024
    }

    private var _viewState: MutableStateFlow<MoveOrderState> = MutableStateFlow(
        MoveOrderState(bayers = emptyList())
    )
    val viewState: StateFlow<MoveOrderState> = _viewState

    private var _soundViewState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val soundViewState: StateFlow<Boolean> = _soundViewState

    // Barcode scan observer
    private var _barcodeState: MutableStateFlow<BarcodeReadState> =
        MutableStateFlow(BarcodeReadState(lastBarcode = ""))
    val barcodeState: StateFlow<BarcodeReadState> = _barcodeState
    private var barcodeJob: Job? = null

    fun observeBarcodes() {
        barcodeJob?.cancel()
        Log.e("onKeyDown_SCAN_3", "LAUNCH")
        barcodeJob = observeBarcodeDataUseCase.observe().onEach { bar ->
            Log.e("onKeyDown_SCAN_4", bar)
            Log.e("onKeyDown_SCAN_4", "${_viewState.value.orderItemForScan}")
            if (_viewState.value.orderItemForScan == null) {
                searchOrderItem(bar)
            } else {
                checkInputSerials(
                    orderItemForScan = _viewState.value.orderItemForScan!!,
                    barcodeSerial = bar
                )
            }
        }.launchIn(viewModelScope)
        observeBarcodeDataUseCase(Unit)
    }

    fun clearOrderItemForScan() {
        Log.e("onKeyDown_SCAN_4", "CLEAR !!")
        _viewState.update { _viewState.value.copy(orderItemForScan = null) }
    }

    fun setManualOrderItemForScan(orderItem: MoveOrderItemsModel) {
        val selectedOrder = _viewState.value.selectedOrder
        val prevOrderForScan = _viewState.value.orderItemForScan
        if (prevOrderForScan?.id == orderItem.id) {
            _viewState.update {
                _viewState.value.copy(
                    orderItemForScan = null,
                    showManualEnterBarcode = false
                )
            }
            return
        }

        if (selectedOrder != null) {
            _viewState.update {
                _viewState.value.copy(
                    orderItemForScan = orderItem,
                    showManualEnterBarcode = false
                )
            }
        }
    }

    fun searchOrderItem(barcode: String) {
        var searchedOrderItem: MoveOrderItemsModel? = null
        val moveOrderItems = viewState.value.selectedOrder?.moveOrderItemsModels
        moveOrderItems?.forEach { orderItem ->
            orderItem.mfgPartNumExp?.apply {
                val barcodePatterns = this.split("/").toMutableList()
                barcodePatterns.add(orderItem.mfrCode)
                barcodePatterns.forEach { pattern ->
                    if (barcode.contains(pattern)) {
                        Log.e("onKeyDown_SCAN_4", "$orderItem")
                        searchedOrderItem = orderItem
                        val selectedOrder = _viewState.value.selectedOrder
                        val orderItems = selectedOrder!!.moveOrderItemsModels.toMutableList()
                        orderItems.remove(orderItem)
                        orderItems.add(0, orderItem)
                        val order = selectedOrder.copy(moveOrderItemsModels = orderItems.toList())
                        _viewState.update {
                            _viewState.value.copy(
                                orderItemForScan = orderItem,
                                showManualEnterBarcode = false,
                                selectedOrder = order
                            )
                        }
                    }
                }
            }
        }
        if (searchedOrderItem == null) {
            _viewState.update {
                _viewState.value.copy(
                    errorData = ErrorData(
                        status = SERIAL_NUMBER_NOT_FOUND,
                        message = barcode,
                        error = "Barcode not found!"
                    ),
                    showManualEnterBarcode = false
                )
            }
        }
    }

    fun checkInputSerials(orderItemForScan: MoveOrderItemsModel, barcodeSerial: String) {
        viewModelScope.launch {
            checkInputSerialsUseCase.invoke(barcodeSerial).onSuccess { error ->
                if (error.status == 0) {
                    addSerial(orderItemForScan, barcodeSerial)
                } else {
                    _viewState.update {
                        _viewState.value.copy(errorData = error)
                    }
                }
            }
        }
    }

    fun addSerial(orderItemForScan: MoveOrderItemsModel, barcodeSerial: String) {
        val serials = viewState.value.itemSerials.toMutableList()
        val orders = viewState.value.moveOrders
        var order: MoveOrderModel? = null
        var orderItemForDelete: MoveOrderItemsModel? = null
        orders.forEach {
            it.value.forEach { o ->
                if (o.moveOrderModel.id == orderItemForScan.moveOrderId) {
                    order = o.moveOrderModel
                    return@forEach
                }
            }
        }
        val itemSerials: ArrayList<ItemSerialModel> = ArrayList()
        serials.forEach {
            if (it.moveOrderItemId == orderItemForScan.id) {
                itemSerials.add(it)
            }
        }
        if (orderItemForScan.quantity <= itemSerials.size) {
            _viewState.update {
                _viewState.value.copy(error = "Перевищено кількість додаданих серійних номерів, максимальна кількість ${orderItemForScan.quantity.toInt()}")
            }
            return
        }
        val itemSerial = ItemSerialModel(
            serial = barcodeSerial,
            moveOrderItemId = orderItemForScan.id
        )
        serials.add(itemSerial)
        saveItemSerialToDb(itemSerial)
        Log.i("SERIALS_", "${order?.isComplete}")
        val updatedMoveOrderItem: MoveOrderItemsModel =
            orderItemForScan.copy(qtyGiven = if (order != null && order.isComplete) orderItemForScan.qtyGiven else orderItemForScan.qtyGiven + 1)
        Log.d("SERIALS_S", "${updatedMoveOrderItem.qtyGiven}")
        var unselectItemForScan = if (order?.isComplete == null) true else !order.isComplete
        val selectedOrder = _viewState.value.selectedOrder
        val orderItems = selectedOrder?.moveOrderItemsModels
        if (orderItems != null) {
            val list = orderItems.toMutableList()
            orderItemForDelete = list.find { it.id == updatedMoveOrderItem.id }
            list.remove(orderItemForDelete)
            list.add(0, updatedMoveOrderItem)
            val order =
                selectedOrder.copy(moveOrderItemsModels = list.toList()) //.sortedBy { it.id }
            val serialSize = serials.filter { it.moveOrderItemId == updatedMoveOrderItem.id }.size
            if (serialSize == updatedMoveOrderItem.qtyGiven.toInt() && serialSize == updatedMoveOrderItem.quantity.toInt())
                unselectItemForScan = true
            _viewState.update { state ->
                state.copy(
                    itemSerials = serials.toList(),
                    showManualEnterBarcode = false,
                    orderItemForScan = if (unselectItemForScan) null else updatedMoveOrderItem,
                    selectedOrder = order
                )
            }
            if (unselectItemForScan)
                _soundViewState.update { true }
        }
        /*_viewState.update {
            _viewState.value.copy(
                itemSerials = serials.toList(),
                showManualEnterBarcode = false,
                orderItemForScan = if(unselectItemForScan) null else updatedMoveOrderItem
            )
        }*/
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

    fun loadMoveOrders(documentType: DocumentType) {
        clearOrderItemForScan()
        viewModelScope.launch {
            loadMoveOrdersUseCase.invoke(documentType).onSuccess { result ->
                val headerStateList = ArrayList<HeaderState>()
                result.forEach {
                    headerStateList.add(HeaderState(header = it.key, isExpand = true))
                }
                _viewState.update {
                    it.copy(
                        moveOrders = result,
                        errorData = null,
                        headerStateList = headerStateList.toList(),
                        documentType = documentType
                    )
                }
                getSavedSerials()
            }.onFailure {
                _viewState.update { it.copy(error = "$it") }
            }
        }
    }

    fun cancelError() {
        _viewState.update { it.copy(error = "", errorData = null) }
    }

    fun showManualBarcodeEntering() {
        _viewState.update { it.copy(showManualEnterBarcode = true) }
    }

    fun cancelManualBarcodeEntering() {
        _viewState.update { it.copy(showManualEnterBarcode = false) }
    }

    fun showQuantityEntering() {
        _viewState.update { it.copy(showQuantityEntering = true) }
    }

    fun cancelQuantityEntering() {
        _viewState.update { it.copy(showQuantityEntering = false) }
    }

    fun changeGroupState(groupName: String) {
        val header = _viewState.value.headerStateList.find { it.header == groupName }
        header?.let {
            val newHeaderState = it.copy(isExpand = !it.isExpand)
            val newHeaderStateList = _viewState.value.headerStateList.toMutableList()
            newHeaderStateList.remove(header)
            newHeaderStateList.add(newHeaderState)
            Log.e("groupName ->", "$newHeaderState")
            _viewState.update { headers -> headers.copy(headerStateList = newHeaderStateList.toList()) }
        }
    }

    fun postSelectedMoveOrder(moveOrder: OrderModel?) {
        _viewState.update { it.copy(selectedOrder = moveOrder) }
    }

    fun getSavedSerials() {
        viewModelScope.launch {
            getItemSerialFromDBUseCase.invoke(Unit).onSuccess { serials ->
                _viewState.update { it.copy(itemSerials = serials) }
            }.onFailure { ex ->
                _viewState.update { it.copy(error = ex.toString()) }
            }
        }
    }

    fun deleteSerial(itemSerialModel: ItemSerialModel) {
        viewModelScope.launch {
            deleteSerialNumberUseCase.invoke(itemSerialModel).onSuccess {
                val selectedOrder = _viewState.value.selectedOrder
                val orderItems = selectedOrder?.moveOrderItemsModels
                orderItems?.forEach { item ->
                    if (item.id == itemSerialModel.moveOrderItemId) {
                        if (item.qtyGiven >= 1) {
                            setQuantityGiven(item, (item.qtyGiven - 1).toInt())
                        }
                    }
                }
                getSavedSerials()
            }
        }
    }

    fun setQuantityGiven(
        moveOrderItemsModel: MoveOrderItemsModel,
        qtyGiven: Int,
        noSerials: Boolean = false
    ) {
        val selectedOrder = _viewState.value.selectedOrder
        val orderItems = selectedOrder?.moveOrderItemsModels
        var orderItemForScan: MoveOrderItemsModel? = null
        var orderItemForDelete: MoveOrderItemsModel? = null
        if (moveOrderItemsModel.quantity < qtyGiven) {
            _viewState.update {
                _viewState.value.copy(error = "Перевищено кількість додаданих серійних номерів, максимальна кількість ${moveOrderItemsModel.quantity.toInt()}")
            }
            return
        }
        viewModelScope.launch {
            setQuantityGivenUseCase.invoke(
                QuantityGivenModel(
                    quantityGiven = qtyGiven,
                    moveOrderItemId = moveOrderItemsModel.id
                )
            ).onSuccess {
                val updateOrderForScan = moveOrderItemsModel.copy(qtyGiven = qtyGiven.toDouble())
                orderItems?.forEach { o ->
                    if (o.id == moveOrderItemsModel.id) {
                        orderItemForDelete = o
                        orderItemForScan = o.copy(qtyGiven = qtyGiven.toDouble())
                        return@forEach
                    }
                }
                if (orderItems != null && orderItemForDelete != null) {
                    val list = orderItems.toMutableList()
                    list.remove(orderItemForDelete)
                    list.add(updateOrderForScan)
                    val order =
                        selectedOrder.copy(moveOrderItemsModels = list.sortedBy { it.id }.toList())
                    _viewState.update { state ->
                        state.copy(
                            orderItemForScan = if (noSerials) null else orderItemForScan,
                            selectedOrder = order,
                            showQuantityEntering = false
                        )
                    }
                    if (noSerials)
                        _soundViewState.update { true }
                }
                //getSavedSerials()
            }
        }
    }

    fun setNoSerials() {
        viewModelScope.launch {
            val orderItem = _viewState.value.orderItemForScan
            val selectedOrder = _viewState.value.selectedOrder
            val orderItems = selectedOrder?.moveOrderItemsModels
            var orderItemForScan: MoveOrderItemsModel? = null
            var orderItemForDelete: MoveOrderItemsModel? = null
            orderItem?.let { orderItem ->
                setNoSerialsUseCase.invoke(orderItem.id).onSuccess {
                    val updateOrderForScan = orderItem.copy(noSerials = true)
                    orderItems?.forEach { o ->
                        if (o.id == orderItem.id) {
                            orderItemForDelete = o
                            orderItemForScan = o.copy(noSerials = true)
                            return@forEach
                        }
                    }
                    if (orderItems != null && orderItemForDelete != null) {
                        val list = orderItems.toMutableList()
                        list.remove(orderItemForDelete)
                        list.add(orderItemForScan!!)
                        val order =
                            selectedOrder.copy(moveOrderItemsModels = list.sortedBy { it.id }
                                .toList())
                        _viewState.update { state ->
                            state.copy(
                                orderItemForScan = updateOrderForScan,
                                selectedOrder = order
                            )
                        }
                    }
                    orderItemForScan?.let {
                        if (it.quantity == 1.0)
                            setQuantityGiven(
                                moveOrderItemsModel = it,
                                qtyGiven = 1,
                                noSerials = true
                            )
                    }
                }
            }
        }
    }

    fun isAllMoveOrdersItemDone(
        moveOrderItems: List<MoveOrderItemsModel>,
        serials: List<ItemSerialModel>
    ): Boolean {
        var itemsDone = 0

        moveOrderItems.forEach { item ->
            val serialsList = serials.filter { it.moveOrderItemId == item.id }
            if (item.qtyGiven == item.quantity && item.noSerials) {
                itemsDone++
            } else if (item.qtyGiven == item.quantity && serialsList.size == item.qtyGiven.toInt()) {
                itemsDone++
            }
        }
        return itemsDone == moveOrderItems.size
    }

    fun setSelectedScrollIndex(idx: Int) {
        Log.i("SelectedScrollIndex", "$idx")
        _viewState.update { state ->
            state.copy(
                selectedIndexForScroll = idx
            )
        }
    }

    fun setSoundPlayed() {
        _soundViewState.update { false }
    }
}