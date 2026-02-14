package com.km.warehouse.ui.move_order

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
//import androidx.compose.ui.input.key.KeyEvent
import android.view.KeyEvent
import androidx.compose.material3.Icon
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
import com.km.warehouse.ui.move_order.scan.ScanMoveOrderView
import com.km.warehouse.ui.sync.ErrorDialog
import org.koin.androidx.compose.koinViewModel

/**
 * Create by Pustovit Oleksandr on 9/24/2025
 */

@Composable
fun MoveOrderView(
    modifier: Modifier,
    documentType: DocumentType,
    onBackClick: () -> Unit
) {
    BackHandler {
        onBackClick.invoke()
    }
    val viewModel: MoveOrderItemViewModel = koinViewModel()
    val state = viewModel.viewState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.loadMoveOrders(documentType)
    }
    if (state.value.selectedOrder != null) {
        ScanMoveOrderView(onBackClick = {
            viewModel.postSelectedMoveOrder(moveOrder = null)
            viewModel.loadMoveOrders(documentType)
        })
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp
        )
        Spacer(modifier = Modifier.width(16.dp))
        MoveOrdersList(
            moveOrders = state.value.moveOrders,
            serials = state.value.itemSerials,
            headerStateList = state.value.headerStateList,
            onHeaderClick = { groupName ->
                viewModel.changeGroupState(groupName)
            },
            onSelectMoveOrderItem = {
                viewModel.setManualOrderItemForScan(it)
            },
            onMoveOrderClick = {
                viewModel.postSelectedMoveOrder(it)
            })
        if (state.value.itemSerials.isNotEmpty()) {
            MoveOrdersList(
                moveOrders = state.value.moveOrders,
                serials = state.value.itemSerials,
                headerStateList = state.value.headerStateList,
                onHeaderClick = { groupName ->
                    viewModel.changeGroupState(groupName)
                },
                onSelectMoveOrderItem = {
                    viewModel.setManualOrderItemForScan(it)
                },
                onMoveOrderClick = {
                    viewModel.postSelectedMoveOrder(it)
                })
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
        if (state.value.errorData != null) {

        }
    }
}

@Composable
fun MoveOrdersList(
    moveOrders: HashMap<String, List<OrderModel>>,
    serials: List<ItemSerialModel>,
    headerStateList: List<HeaderState>,
    onHeaderClick: (String) -> Unit,
    onMoveOrderClick: (OrderModel) -> Unit,
    onSelectMoveOrderItem: (MoveOrderItemsModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .focusable()
    ) {
        moveOrders.forEach {
            val headerState = headerStateList.find { h -> h.header == it.key }!!
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { onHeaderClick(it.key) })
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        text = it.key,
                        color = colorResource(R.color.black),
                        fontSize = 20.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                    Icon(
                        painter = painterResource(id = if (headerState.isExpand) R.drawable.ic_expand else R.drawable.ic_expand_more),
                        contentDescription = null, modifier = Modifier.weight(weight = 0.2f)
                    )
                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 1.dp
                )

                if (!headerState.isExpand)
                    return@item

                it.value.forEach { moveOrder ->
                    Spacer(modifier = Modifier.height(8.dp))
                    MoveOrderHeader(
                        order = moveOrder,
                        onMoveOrderClick = onMoveOrderClick
                    )
                    moveOrder.moveOrderItemsModels.forEach { item ->
                        MoveOrderItemView(
                            item,
                            serials,
                            onDeleteSerial = {},
                            onSelectMoveOrderItem = onSelectMoveOrderItem)
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
fun MoveOrderHeader(
    order: OrderModel,
    onMoveOrderClick: (OrderModel) -> Unit
) {
    val moveOrder = order.moveOrderModel
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onMoveOrderClick(order) }),
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