package com.km.warehouse.ui.move_order.scan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.km.warehouse.R
import com.km.warehouse.ui.move_order.MoveOrderItemViewModel

/**
 * Create by Pustovit Oleksandr on 2/10/2026
 */
@Composable
fun ManualButtons(
    onDismiss: () -> Unit,
    onAccept: () -> Unit
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
            onClick = { onAccept() },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = stringResource(R.string.barcode_accept),
                fontSize = 18.sp
            )
        }
    }
}