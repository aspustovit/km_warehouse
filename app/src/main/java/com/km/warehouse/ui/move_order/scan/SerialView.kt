package com.km.warehouse.ui.move_order.scan

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.km.warehouse.domain.usecase.model.ItemSerialModel
import com.km.warehouse.domain.usecase.model.MoveOrderItemsModel
import com.km.warehouse.ui.move_order.MoveOrderItemView
import com.km.warehouse.ui.move_order.MoveOrderItemViewModel

/**
 * Create by Pustovit Oleksandr on 16/03/2026
 */

@Composable
fun SerialView(
    onBackClick: () -> Unit,
    viewModel: MoveOrderItemViewModel,
    orderItemForScan: MoveOrderItemsModel,
    serials: List<ItemSerialModel>
) {
    BackHandler {
        onBackClick.invoke()
    }
    NoSerialsView(onAcceptClick = { isNoSerials ->
        if (isNoSerials)
            viewModel.setNoSerials()
    }, onManualBarcodeEnterClick = {
        viewModel.showManualBarcodeEntering()
    }, onQuantityClick = {
        viewModel.showQuantityEntering()
    })
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant,
        thickness = 1.dp
    )
    Log.d("SerialView", "$orderItemForScan")
    MoveOrderItemView(
        item = orderItemForScan,
        serials = serials,
        showDeleteBtn = true,
        orderItemForScan = orderItemForScan,
        onDeleteSerial = {
            viewModel.deleteSerial(it)
        },
        onSelectMoveOrderItem = {
        },
        itemInEditMode = true)
}