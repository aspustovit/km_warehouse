package com.km.warehouse.ui.move_order.scan

import android.util.Log
import android.view.KeyEvent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.km.warehouse.R
import com.km.warehouse.domain.usecase.model.ItemSerialModel
import com.km.warehouse.domain.usecase.model.MoveOrderItemsModel
import com.km.warehouse.domain.usecase.model.MoveOrderModel
import com.km.warehouse.ui.SharedViewModel
import com.km.warehouse.ui.move_order.BayerView
import com.km.warehouse.ui.move_order.MoveOrderItemView
import com.km.warehouse.ui.move_order.MoveOrderItemViewModel
import com.km.warehouse.ui.move_order.MoveOrderItemViewModel.Companion.SERIAL_NUMBER_NOT_FOUND
import com.km.warehouse.ui.sync.ErrorDialog
import org.koin.androidx.compose.koinViewModel

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
    if(state.value.showQuantityEntering) {
        EnterQuantityDialog(onDismiss = {
            viewModel.cancelQuantityEntering()
        }, viewModel = viewModel)
    }
    state.value.selectedOrder?.let {
        ManualSerialSearchView(viewModel)
        NoSerialsView(onAcceptClick = { isNoSerials ->
            if(isNoSerials)
                viewModel.setNoSerials()
        }, onCancelClick = {
            onBackClick.invoke()
            viewModel.viewState.value.documentType?.let { dt ->
                viewModel.loadMoveOrders(dt)
            }
        },onQuantityClick = {
            viewModel.showQuantityEntering()
        })
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp
        )
        BayerView(isExpand = false, showExpand = false, key = bayerName, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp))
        MoveOrderHeaderView(moveOrder = it.moveOrderModel)
        ScanMoveOrderItemList(
            viewModel = viewModel,
            moveOrderItemsModels = it.moveOrderItemsModels,
            serials = state.value.itemSerials,
            orderItemForScan = state.value.orderItemForScan
        )
    }
    state.value.errorData?.let {
        if (it.status == SERIAL_NUMBER_NOT_FOUND) {
            ErrorDialog(
                errorMessage = "${stringResource(R.string.barcode)} ${it.message} ${
                    stringResource(
                        R.string.barcode_not_found_error
                    )
                }", onDismiss = {
                    viewModel.cancelError()
                })
        } else {
            ErrorDialog(errorMessage = it.getErrorMessage(), onDismiss = {
                viewModel.cancelError()
            })
        }
    }
    if (state.value.error.isNotBlank()) {
        ErrorDialog(errorMessage = state.value.error, onDismiss = { viewModel.cancelError() })
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
        Spacer(modifier = Modifier.height(4.dp))
        Row {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = moveOrder.number,
                color = colorResource(R.color.black),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = moveOrder.creationDate.replace("T", " "),
                color = colorResource(R.color.black),
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Right
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = moveOrder.description,
            color = colorResource(R.color.black),
            fontSize = 12.sp
        )
        Row {
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
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun ScanMoveOrderItemList(
    viewModel: MoveOrderItemViewModel,
    moveOrderItemsModels: List<MoveOrderItemsModel>,
    orderItemForScan: MoveOrderItemsModel? = null,
    serials: List<ItemSerialModel>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .focusable()) {
        moveOrderItemsModels.forEach { serilas ->
            item {
                MoveOrderItemView(
                    item = serilas,
                    serials,
                    showDeleteBtn = true,
                    orderItemForScan = orderItemForScan,
                    onDeleteSerial = {
                        viewModel.deleteSerial(it)
                    },
                    onSelectMoveOrderItem ={
                        viewModel.setManualOrderItemForScan(it)
                    })

            }
        }
    }
}