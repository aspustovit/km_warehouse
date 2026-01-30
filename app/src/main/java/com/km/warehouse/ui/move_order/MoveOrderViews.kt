package com.km.warehouse.ui.move_order

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
//import androidx.compose.ui.input.key.KeyEvent
import android.view.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.km.warehouse.R
import com.km.warehouse.domain.usecase.model.ItemSerialModel
import com.km.warehouse.domain.usecase.model.MoveOrderItemsModel
import com.km.warehouse.domain.usecase.model.MoveOrderModel
import com.km.warehouse.domain.usecase.model.OrderModel
import com.km.warehouse.ui.SharedViewModel
import com.km.warehouse.ui.move_order.MoveOrderItemViewModel.Companion.SERIAL_NUMBER_NOT_FOUND
import com.km.warehouse.ui.sync.ErrorDialog
import com.km.warehouse.ui.sync.SyncViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Create by Pustovit Oleksandr on 9/24/2025
 */

@Composable
fun MoveOrderView(
    modifier: Modifier,
    onWidgetClicked: () -> Unit,
    onBackClick: () -> Unit
) {
    BackHandler {
        onBackClick.invoke()
    }
    val viewModel: MoveOrderItemViewModel = koinViewModel()
    val scanViewModel: SharedViewModel = koinViewModel()
    val state = viewModel.viewState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.loadMoveOrders()
        viewModel.observeBarcodes()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        ManualSerialSearchView(viewModel)
        /*Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = state.value.bayers.size.toString())
        }*/
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp
        )
        Spacer(modifier = Modifier.width(16.dp))
        MoveOrdersList(state.value.moveOrders, state.value.itemSerials, scanViewModel)
        if (state.value.itemSerials.isNotEmpty()) {
            MoveOrdersList(state.value.moveOrders, state.value.itemSerials, scanViewModel)
        }
        state.value.errorData?.let {
            if (it.status == SERIAL_NUMBER_NOT_FOUND){
                ErrorDialog(errorMessage = "${stringResource(R.string.barcode)} ${it.message} ${stringResource(R.string.barcode_not_found_error)}", onDismiss = {
                    viewModel.cancelError()
                })
            } else {
                ErrorDialog(errorMessage = it.getErrorMessage(), onDismiss = {
                    viewModel.cancelError()
                })
            }
        }
        if (state.value.errorData != null) {

        }
    }
}

@Composable
fun MoveOrdersList(moveOrders: HashMap<String, List<OrderModel>>,
                   serials: List<ItemSerialModel>,
                   scanViewModel: SharedViewModel ) {
    LazyColumn(modifier = Modifier.fillMaxSize().focusable().onPreviewKeyEvent { e ->
        val native = e.nativeKeyEvent
        if (native.action == KeyEvent.ACTION_DOWN) {
            val pressedKey = native.unicodeChar.toChar()
            Log.d("NAV_CONTROLLER", "$pressedKey")
            scanViewModel.onBarcodeScan(pressedKey)
        }
        if (native.action == KeyEvent.ACTION_DOWN && native.keyCode == KeyEvent.KEYCODE_ENTER) {
            scanViewModel.postBarcode()
        }
        false
    }) {
        moveOrders.forEach {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it.key,
                    color = colorResource(R.color.black),
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif
                )
                it.value.forEach { moveOrder ->
                    Spacer(modifier = Modifier.height(8.dp))
                    MoveOrderHeader(moveOrder.moveOrderModel)
                    moveOrder.moveOrderItemsModels.forEach { item ->
                        MoveOrderItemView(item, serials)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun MoveOrderItemView(item: MoveOrderItemsModel, serials: List<ItemSerialModel>) {
    Spacer(modifier = Modifier.height(8.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.sync_dialog),
            contentColor = colorResource(R.color.black)
        )
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(text = stringResource(R.string.inventory_id), fontSize = 16.sp)
            Text(
                text = item.inventoryId.toString(),
                fontSize = 16.sp,
                color = colorResource(R.color.color_text_secondary)
            )
        }
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(text = stringResource(R.string.item_segment), fontSize = 16.sp)
            Text(
                text = item.itemSegment ?: "",
                fontSize = 16.sp,
                color = colorResource(R.color.color_text_secondary)
            )
        }
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(text = item.description, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }
       // Spacer(modifier = Modifier.height(8.dp))
        Log.i("SERIALS", "${item.moveOrderId} = list $serials")
        serials.filter { it.moveOrderItemId == item.id }.forEach { serial ->
            Log.e("_SERIALS_", "Upadte list $serials")
            Column {
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(colorResource(R.color.color_text_secondary))
                )
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    text = serial.serial,
                    fontSize = 14.sp,
                    color = colorResource(R.color.color_text_secondary),
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}

@Composable
fun MoveOrderHeader(moveOrder: MoveOrderModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (moveOrder.isComplete) colorResource(R.color.move_order_complete) else colorResource(
                R.color.sync_dialog
            ),
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
fun ManualSerialSearchView(viewModel: MoveOrderItemViewModel) {
    val state = viewModel.viewState.collectAsState()
    Row(verticalAlignment = Alignment.CenterVertically) {
        /*Box(modifier = Modifier.weight(1.5f)) {
            OutlinedTextField(
                value = searchNumber,
                onValueChange = { searchNumber = it },
                label = { Text(stringResource(R.string.barcode_number)) },
                singleLine = true,
                enabled = isEditTextEnabled,
                modifier = Modifier.focusRequester(focusRequester),
                )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(onClick = {
                        isEditTextEnabled = true
                        focusRequester.requestFocus()
                    }),
            )
        }

        Spacer(modifier = Modifier.width(8.dp))*/
        Button(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            onClick = {
                viewModel.showManualBarcodeEntering()
            },
        ) {
            Text(text = stringResource(R.string.manual_barcode), fontSize = 16.sp)
        }
        if(state.value.showManualEnterBarcode)
            ManualBarcodeEnterDialog(viewModel = viewModel, onDismiss = {
                viewModel.cancelManualBarcodeEntering()
            })
    }
}