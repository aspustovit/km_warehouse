package com.km.warehouse.ui.move_order.scan

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.km.warehouse.R
import com.km.warehouse.ui.move_order.ManualBarcodeEnterDialog
import com.km.warehouse.ui.move_order.MoveOrderItemViewModel

/**
 * Create by Pustovit Oleksandr on 2/10/2026
 */

@Composable
fun ManualSerialSearchView(viewModel: MoveOrderItemViewModel) {
    val state = viewModel.viewState.collectAsState()
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
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