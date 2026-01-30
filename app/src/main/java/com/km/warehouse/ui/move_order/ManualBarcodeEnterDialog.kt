package com.km.warehouse.ui.move_order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.km.warehouse.R

/**
 * Create by Pustovit Oleksandr on 1/29/2026
 */

@Composable
fun ManualBarcodeEnterDialog(
    onDismiss: () -> Unit,
    viewModel: MoveOrderItemViewModel
) {
    var searchNumber by remember { mutableStateOf("0193015506459") }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(310.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.sync_dialog),
                contentColor = colorResource(R.color.black)
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.manual_barcode_header),
                    modifier = Modifier.padding(16.dp),
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    value = searchNumber,
                    onValueChange = { searchNumber = it },
                    label = { Text(stringResource(R.string.barcode_number)) },
                    singleLine = true
                )

                ManualButtons(onDismiss, viewModel, searchNumber)
            }
        }
    }
}

@Composable
private fun ManualButtons(
    onDismiss: () -> Unit,
    viewModel: MoveOrderItemViewModel,
    searchNumber: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        TextButton(
            onClick = { onDismiss() },
            modifier = Modifier.padding(8.dp),
        ) {
            Text(text = stringResource(R.string.cancel), fontSize = 18.sp)
        }
        TextButton(
            onClick = { viewModel.searchBarcodeInOrderItems(barcode = searchNumber) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = stringResource(R.string.barcode_accept),
                fontSize = 18.sp
            )
        }
    }
}