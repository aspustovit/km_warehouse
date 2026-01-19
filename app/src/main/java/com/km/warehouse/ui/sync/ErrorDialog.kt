package com.km.warehouse.ui.sync

import android.graphics.Color
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.km.warehouse.R

/**
 * Create by Pustovit Oleksandr on 1/8/2026
 */

@Composable
fun ErrorDialog(
    errorMessage: String,
    onDismiss: () -> Unit
) {
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
                text = errorMessage,
                color = colorResource(R.color.white),
                fontSize = 16.sp
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = stringResource(R.string.ok),
                    color = colorResource(R.color.white),
                    fontSize = 18.sp)
            }
        },
        containerColor = colorResource(R.color.error)
    )
}