package com.km.warehouse

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import android.view.KeyEvent.KEYCODE_SHIFT_LEFT
import android.view.KeyEvent.KEYCODE_SHIFT_RIGHT
import android.view.KeyEvent.KEYCODE_TV_ANTENNA_CABLE
import android.view.KeyEvent.KEYCODE_TV_SATELLITE_SERVICE
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.km.warehouse.ui.CustomOutlinedImageButton
import com.km.warehouse.ui.DarkTopAppBar
import com.km.warehouse.ui.SharedViewModel
import com.km.warehouse.ui.sync.SyncDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    val viewModel: SharedViewModel by viewModel()

    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(e: KeyEvent): Boolean {
        Log.d("NAV_CONTROLLER", "$e")
        if (e.action == KeyEvent.ACTION_DOWN && e.keyCode != KEYCODE_TV_SATELLITE_SERVICE
            && e.keyCode != KEYCODE_SHIFT_LEFT && e.keyCode != KEYCODE_SHIFT_RIGHT
            && e.keyCode != KEYCODE_BACK && e.keyCode != KEYCODE_TV_ANTENNA_CABLE) {
            val pressedKey = e.unicodeChar.toChar()
            Log.i("NAV_CONTROLLER", "$pressedKey")
            if (e.keyCode != KeyEvent.KEYCODE_ENTER)
                viewModel.onBarcodeScan(pressedKey)
        }
        if (e.action == KeyEvent.ACTION_DOWN && e.keyCode == KeyEvent.KEYCODE_ENTER) {
            viewModel.postBarcode()
            return false
        }
        return super.dispatchKeyEvent(e)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            MaterialTheme { // Apply your app's theme
                NavigationController(modifier = Modifier.fillMaxSize()) // Call your screen's composable
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    activity: ComponentActivity,
    onApprovedDocumentClick: () -> Unit = {},
    onIssuedDocumentClick: () -> Unit = {},
    onIncomeDocumentClick: () -> Unit = {},
    onScanToFileClick: () -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        SyncDialog(
            onDismissRequest = {
                showDialog = false
            },
            painter = painterResource(R.drawable.ic_internet_connection_phone),
            imageDescription = ""
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DarkTopAppBar(
            title = { Text(stringResource(id = R.string.main_screen_title)) },
            modifier = Modifier.fillMaxWidth()
        )
        CustomOutlinedImageButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            startIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_barcode_scanner),
                    contentDescription = null
                )
            },
            onClick = { onApprovedDocumentClick.invoke() },
            enabled = true,
            text = {
                Text(
                    text = stringResource(id = R.string.approved_documents),
                    fontSize = 16.sp
                )
            })

        CustomOutlinedImageButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            startIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_document),
                    contentDescription = null
                )
            },
            onClick = {
                onIssuedDocumentClick.invoke()
            },
            enabled = true,
            text = {
                Text(
                    text = stringResource(id = R.string.issued_documents),
                    fontSize = 16.sp
                )
            })

        CustomOutlinedImageButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            startIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_income_documents),
                    contentDescription = null
                )
            },
            onClick = {
                onIncomeDocumentClick.invoke()
            },
            enabled = true,
            text = {
                Text(
                    text = stringResource(id = R.string.income_documents),
                    fontSize = 16.sp
                )
            })

        CustomOutlinedImageButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            startIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_barcode_reader),
                    contentDescription = null
                )
            },
            onClick = {
                onScanToFileClick.invoke()
            },
            enabled = true,
            text = {
                Text(
                    text = stringResource(id = R.string.scan_to_file),
                    fontSize = 16.sp
                )
            })

        CustomOutlinedImageButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            startIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_cloud_sync),
                    contentDescription = null
                )

            },
            onClick = {
                showDialog = true
            },
            enabled = true,
            text = {
                Text(
                    text = stringResource(id = R.string.sync),
                    fontSize = 16.sp
                )
            })

        CustomOutlinedImageButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            startIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = null
                )
            },
            onClick = {
                activity.finish()
            },
            enabled = true,
            text = {
                Text(
                    text = stringResource(id = R.string.exit),
                    fontSize = 16.sp
                )
            })
        //
        // Add more UI elements here, like Buttons, Images, etc.
    }

}