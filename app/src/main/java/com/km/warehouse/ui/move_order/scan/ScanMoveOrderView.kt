package com.km.warehouse.ui.move_order.scan

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fieldbee.core.ui.compose.utils.playSound
import com.km.warehouse.R
import com.km.warehouse.domain.usecase.model.ItemSerialModel
import com.km.warehouse.domain.usecase.model.MoveOrderItemsModel
import com.km.warehouse.domain.usecase.model.MoveOrderModel
import com.km.warehouse.ui.SharedViewModel
import com.km.warehouse.ui.move_order.BayerViewSmall
import com.km.warehouse.ui.move_order.ManualBarcodeEnterDialog
import com.km.warehouse.ui.move_order.MoveOrderItemView
import com.km.warehouse.ui.move_order.MoveOrderItemViewModel
import com.km.warehouse.ui.move_order.MoveOrderItemViewModel.Companion.SERIAL_NUMBER_ALREDY_ADD
import com.km.warehouse.ui.move_order.MoveOrderItemViewModel.Companion.SERIAL_NUMBER_ALREDY_FINISH
import com.km.warehouse.ui.move_order.MoveOrderItemViewModel.Companion.SERIAL_NUMBER_NOT_FOUND
import com.km.warehouse.ui.sync.ErrorDialog
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

/**
 * Create by Pustovit Oleksandr on 2/5/2026
 */

@Composable
fun ScanMoveOrderView(
    onBackClick: () -> Unit,
    bayerName: String
) {
    BackHandler {
        onBackClick.invoke()
    }
    val viewModel: MoveOrderItemViewModel = koinViewModel()
    val state = viewModel.viewState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.observeBarcodes()
    }
    if (state.value.showQuantityEntering) {
        EnterQuantityDialog(
            onDismiss = {
                viewModel.cancelQuantityEntering()
            },
            viewModel = viewModel
        )
    }
    state.value.errorData?.let {
        when (it.status) {
            SERIAL_NUMBER_NOT_FOUND -> {
                ErrorDialog(
                    errorMessage = "${stringResource(R.string.barcode)} ${it.message} ${
                        stringResource(
                            R.string.barcode_not_found_error
                        )
                    }", onDismiss = {
                        viewModel.cancelError()
                    })
            }

            SERIAL_NUMBER_ALREDY_FINISH -> {
                ErrorDialog(
                    errorMessage = "${stringResource(R.string.barcode)} ${it.message} ${
                        stringResource(R.string.barcode_already_done)
                    }", onDismiss = {
                        viewModel.cancelError()
                    })
            }

            SERIAL_NUMBER_ALREDY_ADD -> {
                LocalContext.current.playSound(R.raw.windows_error)
                Toast.makeText(LocalContext.current, it.message, Toast.LENGTH_SHORT).show()
            }

            else -> {
                ErrorDialog(errorMessage = it.getErrorMessage(), onDismiss = {
                    viewModel.cancelError()
                })
            }
        }
    }
    if (state.value.error.isNotBlank()) {
        ErrorDialog(errorMessage = state.value.error, onDismiss = { viewModel.cancelError() })
    }

    if (state.value.showManualEnterBarcode) {
        ManualBarcodeEnterDialog(viewModel = viewModel, onDismiss = {
            viewModel.cancelManualBarcodeEntering()
        })
    }

    state.value.selectedOrder?.let {
        //ManualSerialSearchView(viewModel)
        val orderItemForScan = state.value.orderItemForScan
        if (orderItemForScan != null) {
            SerialView(
                onBackClick = { viewModel.clearOrderItemForScan() },
                viewModel = viewModel,
                serials = state.value.itemSerials,
                orderItemForScan = orderItemForScan
            )
            return
        }

        /*NoSerialsView(onAcceptClick = { isNoSerials ->
            if(isNoSerials)
                viewModel.setNoSerials()
        }, onManualBarcodeEnterClick = {
            viewModel.showManualBarcodeEntering()
        },onQuantityClick = {
            viewModel.showQuantityEntering()
        })
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp
        )*/
        BayerViewSmall(
            isExpand = false, showExpand = false, key = bayerName, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
        MoveOrderHeaderView(moveOrder = it.moveOrderModel)
        ScanMoveOrderItemList(
            viewModel = viewModel,
            moveOrderItemsModels = it.moveOrderItemsModels,
            serials = state.value.itemSerials,
            orderItemForScan = state.value.orderItemForScan
        )
    }
}

@Composable
fun MoveOrderHeaderView(moveOrder: MoveOrderModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.sync_dialog),
            contentColor = colorResource(R.color.black)
        )
    ) {
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = moveOrder.number,
            color = colorResource(R.color.black),
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        /*    Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = moveOrder.creationDate.replace("T", " "),
                color = colorResource(R.color.black),
                fontStyle = FontStyle.Normal,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                text = moveOrder.description,
                color = colorResource(R.color.black),
                fontSize = 12.sp
            )*/
        /*Row {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(R.string.move_order_update_date),
                color = colorResource(R.color.black),
                fontStyle = FontStyle.Normal
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = moveOrder.moveDate,
                color = colorResource(R.color.black),
                fontStyle = FontStyle.Normal
            )
        }*/
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Composable
fun ScanMoveOrderItemList(
    viewModel: MoveOrderItemViewModel,
    moveOrderItemsModels: List<MoveOrderItemsModel>,
    orderItemForScan: MoveOrderItemsModel? = null,
    serials: List<ItemSerialModel>
) {
    if (moveOrderItemsModels.size == 1) {
        Log.e("SERIALS_S_H", "${serials.size}")
        MoveOrderItemView(
            item = moveOrderItemsModels.first(),
            serials = serials,
            showDeleteBtn = true,
            orderItemForScan = orderItemForScan,
            onDeleteSerial = {
                viewModel.deleteSerial(it)
            },
            onSelectMoveOrderItem = {
                viewModel.setManualOrderItemForScan(it)
            },
            onEditSerial = {
                viewModel.showManualBarcodeEntering(it)
            })
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .focusable()
        ) {
            Log.i("SERIALS_S_", "${serials.size}")
            items(moveOrderItemsModels) { itemModel ->
                MoveOrderItemView(
                    item = itemModel,
                    serials = serials,
                    showDeleteBtn = true,
                    orderItemForScan = orderItemForScan,
                    onDeleteSerial = {
                        viewModel.deleteSerial(it)
                    },
                    onSelectMoveOrderItem = {
                        viewModel.setManualOrderItemForScan(it)
                    },
                    onEditSerial = {
                        viewModel.showManualBarcodeEntering(it)
                    })
            }
        }
    }
}