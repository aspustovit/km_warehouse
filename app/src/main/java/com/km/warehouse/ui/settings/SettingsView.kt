package com.km.warehouse.ui.settings

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.km.warehouse.R
import com.km.warehouse.ui.DarkTopAppBar
import org.koin.androidx.compose.koinViewModel

/**
* Create by Pustovit Oleksandr on 30/03/2026
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(activity: Activity) {
    val viewModel: SettingsViewModel = koinViewModel()
    viewModel.loadTerminalId()
    Column(modifier = Modifier.fillMaxSize()) {
        var state by rememberSaveable { mutableStateOf(viewModel.viewState.value.terminalId) }
        DarkTopAppBar(
            title = { Text(stringResource(id = R.string.settings)) },
            modifier = Modifier.fillMaxWidth(),
            actions = {
                IconButton(onClick = {
                    viewModel.putTerminalId(state)
                    activity.onBackPressed()
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_save),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.surface
                    )
                }
            }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            value = state,
            onValueChange = {
                state = it
            },
            label = { Text(stringResource(id = R.string.terminal_id), fontSize = 20.sp) },
            enabled = true,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}