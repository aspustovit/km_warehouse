package com.km.warehouse.ui.settings

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.km.warehouse.R
import com.km.warehouse.ui.DarkTopAppBar
import com.km.warehouse.ui.move_order.BayerView
import org.koin.androidx.compose.koinViewModel

/**
 * Create by Pustovit Oleksandr on 30/03/2026
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(activity: Activity) {
    val viewModel: SettingsViewModel = koinViewModel()
    viewModel.loadUserSettings()
    Column(modifier = Modifier.fillMaxSize()) {
        var stateTerminalId by rememberSaveable { mutableStateOf(viewModel.viewState.value.terminalId) }
        val state = viewModel.viewState.collectAsState()
        val checked = remember { mutableStateOf(false) }

        DarkTopAppBar(
            title = { Text(stringResource(id = R.string.settings)) },
            modifier = Modifier.fillMaxWidth(),
            actions = {
                IconButton(onClick = {
                    viewModel.saveSettings(stateTerminalId)
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            value = stateTerminalId,
            onValueChange = {
                stateTerminalId = it
            },
            label = { Text(stringResource(id = R.string.terminal_id), fontSize = 20.sp) },
            enabled = true,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .focusable()
        ) {
            state.value.userWarehouseModel.forEach { key, model ->
                item {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        thickness = 1.dp
                    )

                    BayerView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = { })
                            .padding(vertical = 16.dp,horizontal = 16.dp,),
                        isExpand = true,
                        showExpand = false, key = key
                    )

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        thickness = 1.dp
                    )

                    model.forEach { warehouseModel ->
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 32.dp)) {

                            Checkbox(
                                checked = warehouseModel.isSelected,
                                onCheckedChange = { isChecked -> viewModel.selectWarehouse(warehouseModel, isChecked) }
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                text = warehouseModel.warehouseName
                            )
                        }
                    }
                }
            }
        }

    }
}