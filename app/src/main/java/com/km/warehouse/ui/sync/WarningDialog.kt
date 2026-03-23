package com.km.warehouse.ui.sync

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.fieldbee.core.ui.compose.utils.playSound
import com.km.warehouse.R

/**
 * Create by Pustovit Oleksandr on 23/03/2026
 */

@Composable
fun WarningDialog(
    warningMessage: String,
    onDismiss: () -> Unit,
    onOk: () -> Unit
) {
    LocalContext.current.playSound(R.raw.windows_error)
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.error),
                color = colorResource(R.color.white)
            )
        },
        text = {
            Text(
                text = warningMessage,
                color = colorResource(R.color.white),
                fontSize = 16.sp
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = stringResource(R.string.cancel),
                    color = colorResource(R.color.white),
                    fontSize = 18.sp)
            }

            TextButton(
                onClick = onOk
            ) {
                Text(text = stringResource(R.string.ok_cancel),
                    color = colorResource(R.color.white),
                    fontSize = 18.sp)
            }
        },
        containerColor = colorResource(R.color.new_order)
    )
}