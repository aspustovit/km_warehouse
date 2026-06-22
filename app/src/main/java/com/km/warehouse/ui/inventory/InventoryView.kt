package com.km.warehouse.ui.inventory

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.startActivityForResult
import com.km.warehouse.MainActivity
import com.km.warehouse.R
import com.km.warehouse.ui.DarkTopAppBar
import com.km.warehouse.ui.inventory.full.FilesInventoryList
import com.km.warehouse.ui.sync.ErrorDialog
import com.km.warehouse.ui.sync.Loader
import com.km.warehouse.ui.sync.WarningDialog
import com.km.warehouse.ui.utils.ExcelExporter
import org.koin.androidx.compose.koinViewModel

/**
 * Create by Pustovit Oleksandr on 01/06/2026
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryView(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val viewModel: InventoryViewModel = koinViewModel()
    val state = viewModel.viewState.collectAsState()
    val listState = rememberLazyListState()

    val firstVisibleIndex by remember {
        derivedStateOf { listState.firstVisibleItemIndex }
    }
    LaunchedEffect(viewModel) {
        viewModel.observeBarcodes()
    }

    BackHandler {
        if (state.value.inventory.isEmpty()) {
            onBackClick.invoke()
        } else {
            viewModel.showExitWarning()
        }
    }

    if (state.value.showExitDialog) {
        WarningDialog(
            warningMessage = stringResource(R.string.exit_dialog_message),
            onDismiss = {
                viewModel.cancelExitWarning()
            },
            onOk = {
                onBackClick.invoke()
            }
        )
    }
    state.value.errorData?.let {
        ErrorDialog(errorMessage = it.getErrorMessage(), onDismiss = {
            viewModel.cancelError()
        })
    }

    if(state.value.showFullInventoryScreen) {
        FilesInventoryList(onBackClick = {
            viewModel.cancelFullInventoryScreen()
        })
    } else {
        if (state.value.selectedInventory != null) {
            EditInventoryView(
                state.value.selectedInventory!!,
                onInventorySaveClick = { fact ->
                    viewModel.updateInventory(fact)
                },
                onBackClick = {
                    viewModel.editInventory(null)
                })
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                DarkTopAppBar(
                    title = { Text(stringResource(id = R.string.inventory)) },
                    modifier = Modifier.fillMaxWidth(),
                    actions = {
                        IconButton(onClick = {
                            val dataForXls = viewModel.viewState.value
                            val fileUri = ExcelExporter.export(
                                context,
                                dataForXls.inventory,
                                dataForXls.inventoryBarcode
                            )
                            val emailIntent = ExcelExporter.getIntentForMail(
                                subject = context.getString(R.string.part_inventory),
                                uri = fileUri,
                                context = context
                            )
                            val openInChooser = Intent.createChooser(
                                emailIntent,
                                context.getString(R.string.send_mail_title)
                            )
                            openInChooser.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            context.startActivity(openInChooser)
                            //viewModel.exportToXLS()
                        }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_xsl),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.surface
                            )
                        }
                        IconButton(onClick = {
                            viewModel.showFullInventoryScreen()
                            //viewModel.showImportExelFileDialog()
                        }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_database),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.surface
                            )
                        }

                    }
                )

                if (state.value.inventoryListLoading) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        text = "${stringResource(id = R.string.inventory_search)} ${state.value.inventoryBarcode}",
                        fontSize = 16.sp
                    )
                    Loader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(136.dp)
                            .width(260.dp)
                    )
                } else {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        text = stringResource(id = R.string.inventory_find_message),
                        fontSize = 16.sp
                    )
                    if(state.value.inventoryBarcode.isNotBlank() && state.value.inventory.isEmpty()) {
                        InventoryNotFoundView(barcode = state.value.inventoryBarcode)
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .focusable(),
                    state = listState
                ) {
                    state.value.inventory.forEach {
                        item {
                            InventoryItemView(
                                it,
                                idx = firstVisibleIndex,
                                onInventoryClick = { inv ->
                                    viewModel.editInventory(inv.first)
                                })
                        }
                    }
                }
            }
        }
    }
}