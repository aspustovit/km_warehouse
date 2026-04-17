package com.km.warehouse.ui.move_order.scan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.km.warehouse.R
import com.km.warehouse.ui.OutlinedIconButton

/**
 * Create by Pustovit Oleksandr on 2/9/2026
 */
@Composable
fun NoSerialsView(
    onAcceptClick: (Boolean) -> Unit,
    onQuantityClick: (Boolean) -> Unit,
    onManualBarcodeEnterClick: () -> Unit
) {
    var checkedState by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // Make the entire row clickable to toggle the checkbox
            .clickable { checkedState = !checkedState }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = { isChecked ->
                checkedState = isChecked
            }
        )
        Text(
            text = stringResource(R.string.no_barcode_for_scan),
            fontSize = 13.sp
        )

        //Spacer(modifier = Modifier.width(32.dp))

        OutlinedIconButton(
            onClick = { onAcceptClick.invoke(checkedState) },
            resDrawable = R.drawable.ic_check,
            resDescription = R.string.finish_sync,

        )

        OutlinedIconButton(
            onClick = { onQuantityClick.invoke(checkedState) },
            resDrawable = R.drawable.ic_qnt_count,
            resDescription = R.string.quantity
        )

        OutlinedIconButton(
            onClick = { onManualBarcodeEnterClick.invoke() },
            resDrawable = R.drawable.ic_barcode_manual,
            resDescription = R.string.manual_barcode
        )
    }
    /*SerialActionButton(
        onCancelClick = onCancelClick,
        onAcceptClick = onAcceptClick,
        onQuantityClick = onQuantityClick,
        isBarcodeNeed = checkedState
    )*/
}

@Composable
fun SerialActionButton(
    onAcceptClick: (Boolean) -> Unit,
    onQuantityClick: () -> Unit,
    onCancelClick: () -> Unit,
    isBarcodeNeed: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedIconButton(
            onClick = { onAcceptClick.invoke(isBarcodeNeed) },
            resDrawable = R.drawable.ic_check,
            resDescription = R.string.finish_sync
            )
        OutlinedIconButton(
            onClick = { onQuantityClick.invoke() },
            resDrawable = R.drawable.ic_qnt_count,
            resDescription = R.string.quantity
        )

        OutlinedIconButton(
            onClick = { onCancelClick.invoke() },
            resDrawable = R.drawable.ic_cancel,
            resDescription = R.string.cancel
        )
    }
}