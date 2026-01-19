package com.km.warehouse.ui.scan_to_file

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.km.warehouse.MainActivity
import com.km.warehouse.R
import com.km.warehouse.ui.DarkTopAppBar
import com.km.warehouse.ui.SharedViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Create by Pustovit Oleksandr on 9/26/2025
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanToFileView(activity: Activity) {
    val viewModel: SharedViewModel = koinViewModel()
    val state = viewModel.viewState.collectAsState()
    viewModel.observeBarcodes()
    Column(modifier = Modifier.fillMaxSize()) {
        DarkTopAppBar(
            title = { Text(stringResource(id = R.string.scan_to_file)) },
            modifier = Modifier.fillMaxWidth(),
            actions = {
                IconButton(onClick = {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, state.value.barcodeData)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    activity.startActivity(shareIntent)
                }) {
                    /*Icon(
                        imageVector = ,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.surface
                    )*/
                }
            }
        )
        var text by rememberSaveable { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxSize(),
            value = state.value.barcodeData,
            onValueChange = {
                text = it
            },
            label = { Text(stringResource(id = R.string.file_data), fontSize = 20.sp) },
            enabled = false
        )
    }
}