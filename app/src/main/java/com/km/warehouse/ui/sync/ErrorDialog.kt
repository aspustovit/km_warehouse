package com.km.warehouse.ui.sync

import android.graphics.Color
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import android.view.KeyEvent.KEYCODE_SHIFT_LEFT
import android.view.KeyEvent.KEYCODE_SHIFT_RIGHT
import android.view.KeyEvent.KEYCODE_TV_ANTENNA_CABLE
import android.view.KeyEvent.KEYCODE_TV_SATELLITE_SERVICE
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.core.view.KeyEventDispatcher.dispatchKeyEvent
import com.fieldbee.core.ui.compose.utils.playSound
import com.km.warehouse.R
import com.km.warehouse.ui.SharedViewModel
import com.km.warehouse.ui.move_order.MoveOrderItemViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Create by Pustovit Oleksandr on 1/8/2026
 */

@Composable
fun ErrorDialog(
    errorMessage: String,
    onDismiss: () -> Unit
) {
    LocalContext.current.playSound(R.raw.windows_error)
    //val viewModel: SharedViewModel = koinViewModel()
    AlertDialog(
        /*modifier = Modifier.onKeyEvent { keyEvent ->
            val e = keyEvent.nativeKeyEvent
            if (e.action == KeyEvent.ACTION_DOWN && e.keyCode != KEYCODE_TV_SATELLITE_SERVICE
                && e.keyCode != KEYCODE_SHIFT_LEFT && e.keyCode != KEYCODE_SHIFT_RIGHT
                && e.keyCode != KEYCODE_BACK && e.keyCode != KEYCODE_TV_ANTENNA_CABLE
            ) {
                val pressedKey = e.unicodeChar.toChar()
                Log.i("NAV_CONTROLLER", "$pressedKey")
                //if (e.keyCode != KeyEvent.KEYCODE_ENTER && Character.isLetterOrDigit(pressedKey) || supportedSymbols.contains(pressedKey))
                viewModel.onBarcodeScan(pressedKey)
                true
            }
            Log.v("NAV_CONTROLLER", "${e.keyCode} = ${e.action}")
            if (e.action == KeyEvent.ACTION_DOWN && e.keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.v("NAV_CONTROLLER", "postBarcode")
                viewModel.postBarcode()
                true
            } else {
                false // Not handled
            }
        },*/
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
                Text(
                    text = stringResource(R.string.ok),
                    color = colorResource(R.color.white),
                    fontSize = 18.sp
                )
            }
        },
        containerColor = colorResource(R.color.error)
    )
}