package com.km.warehouse.ui.inventory.full

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.km.warehouse.R
import com.km.warehouse.data.converter.InventoryFileTypes
import com.km.warehouse.domain.usecase.inventory.InventoryFileModel
import com.km.warehouse.ui.DarkTopAppBar
import com.km.warehouse.ui.inventory.EditInventoryView
import com.km.warehouse.ui.inventory.InventoryItemView
import com.km.warehouse.ui.inventory.RenameDialog
import com.km.warehouse.ui.move_order.MoveOrderItemViewModel.Companion.PARCE_FILE_ERROR
import com.km.warehouse.ui.sync.ErrorDialog
import com.km.warehouse.ui.sync.WarningDialog
import com.km.warehouse.ui.utils.ExcelExporter
import org.koin.androidx.compose.koinViewModel

/**
 * Create by Pustovit Oleksandr on 08/06/2026
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilesInventoryList(onBackClick: () -> Unit,
                       fileTypes: InventoryFileTypes = InventoryFileTypes.FULL,
                       onPartFileClick: (Int) -> Unit) {
    val context = LocalContext.current
    val viewModel: FullInventoryViewModel = koinViewModel()
    val state = viewModel.viewState.collectAsState()
    val listState = rememberLazyListState()
    var showRenameDialog by remember { mutableStateOf(false) }
    var showNewFileDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.observeBarcodes()
        viewModel.loadSavedFiles(fileTypes)
    }
    BackHandler {
        onBackClick.invoke()
    }
    val selectFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.loadExelFile(uri)
        Log.e("EXEL", "${uri}")
    }
    if (state.value.showLoadFileDialog) {
        Log.e("EXEL", "SHOW")
        selectFileLauncher.launch("*/*")
    }
    state.value.errorData?.let {
        when (it.status) {
            PARCE_FILE_ERROR -> {
                ErrorDialog(
                    errorMessage = "${stringResource(R.string.file_load_error)}\n ${
                        stringResource(R.string.file_name)
                    } - ${it.message}",
                    onDismiss = {
                        viewModel.cancelError()
                    })
            }

            else -> {
                ErrorDialog(errorMessage = it.getErrorMessage(), onDismiss = {
                    viewModel.cancelError()
                })
            }
        }
    }

    state.value.xlsFileUri?.let {
        val emailIntent = ExcelExporter.getIntentForMail(
            subject = context.getString(R.string.full_inventory),
            uri = it,
            context = context
        )
        val openInChooser = Intent.createChooser(
            emailIntent,
            context.getString(R.string.send_mail_title)
        )
        openInChooser.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(openInChooser)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DarkTopAppBar(
            title = {
                if (state.value.selectedInventoryFile != null) {
                    Text(text = state.value.selectedInventoryFile!!.fileName)
                } else {
                    Text(stringResource(id = R.string.inventory))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            actions = {
                if (state.value.selectedInventoryFile != null) {
                    IconButton(
                        modifier = Modifier.focusable(enabled = true),
                        onClick = {
                            viewModel.searchInventoryInFile("")
                        }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_no_barcodes),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }

                    IconButton(
                        modifier = Modifier.focusable(enabled = true),
                        onClick = {
                            viewModel.exportFullInventoryToExel()
                        }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_xsl),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                } else {
                    if(fileTypes == InventoryFileTypes.FULL) {
                        IconButton(modifier = Modifier.focusable(enabled = true), onClick = {
                            showNewFileDialog = true
                        }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_income_documents),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                }
            }
        )
        if (state.value.selectedInventory != null) {
            EditInventoryView(
                state.value.selectedInventory!!,
                showToolbar = false,
                onInventorySaveClick = { fact ->
                    viewModel.updateInventory(fact)
                },
                onBackClick = {
                    viewModel.editInventory(null)
                })
        }
        if (state.value.selectedInventoryFile != null) {
            FullInventoryItemView(
                fileInventoryModels = state.value.fileInventoryModels,
                onBackClick = { viewModel.removeFileSelection() },
                setAsDoneClick = { fact -> viewModel.updateInventory(fact) },
                onInventoryClick = { inv ->
                    viewModel.editInventory(inv.first)
                },
                barcode = state.value.barcode,
                showNoInventoryMessage = state.value.showNoInventoryMessage
            )
        } else {
            if (state.value.parseExelProgress) {
                ParseXlsFileProgressView()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .focusable(),
                    state = listState
                ) {
                    state.value.dbFiles.forEach {
                        item {
                            FilesItemView(
                                item = it,
                                onFileClick = { inventoryFile ->
                                    when(fileTypes){
                                        InventoryFileTypes.FULL -> viewModel.onInventoryFileSelected(inventoryFile)
                                        InventoryFileTypes.PART -> onPartFileClick(inventoryFile.id)
                                    }

                                }, onEdit = { inventoryFile ->
                                    viewModel.startRenameFile(inventoryFile)
                                    showRenameDialog = true
                                }, onDelete = { inventoryFile ->
                                    viewModel.setInventoryFileForDelete(inventoryFile)
                                }, onExel = { exel ->
                                    viewModel.exportFullInventoryToExel(exel)
                                })
                        }
                    }
                }
            }
        }

        if (state.value.fileModelForDelete != null) {
            WarningDialog(
                warningMessage = stringResource(R.string.delete_inventory_dialog_message),
                onDismiss = {
                    viewModel.cancelFileDelete()
                },
                onOk = {
                    viewModel.deleteFile()
                }
            )
        }

        if (showRenameDialog) {
            RenameDialog(
                onDismiss = {
                    showRenameDialog = false
                    viewModel.finishRename()
                },
                onSave = { newName ->
                    viewModel.rename(newName)
                    showRenameDialog = false
                },
                name = state.value.fileName,
                caption = stringResource(R.string.rename),
                hint = stringResource(R.string.file_name)
            )
        }
        if (showNewFileDialog) {
            RenameDialog(
                onDismiss = {
                    showNewFileDialog = false
                    viewModel.finishRename()
                },
                onSave = { newName ->
                    viewModel.showImportExelFileDialog(newName)
                    showNewFileDialog = false
                },
                name = state.value.fileName,
                caption = stringResource(R.string.new_name_file),
                hint = stringResource(R.string.file_name)
            )
        }

    }
}

fun Modifier.verticalScrollbar(
    state: LazyListState,
    width: Dp = 6.dp,
    color: Color = Color.Gray.copy(alpha = 0.5f)
): Modifier = this.drawWithContent {
    // Draw the list contents first
    drawContent()

    val layoutInfo = state.layoutInfo
    val totalItemsCount = layoutInfo.totalItemsCount

    // Only draw if there are items and content overflows the view
    if (totalItemsCount > 0) {
        val firstVisibleItem = state.firstVisibleItemIndex
        val visibleItemsCount = layoutInfo.visibleItemsInfo.size

        if (visibleItemsCount < totalItemsCount) {
            // Calculate ratios for position and height
            val scrollProgress = firstVisibleItem.toFloat() / totalItemsCount
            val sizeProgress = visibleItemsCount.toFloat() / totalItemsCount

            val scrollbarHeight = size.height * sizeProgress
            val scrollbarOffsetY = size.height * scrollProgress

            drawRoundRect(
                color = color,
                topLeft = Offset(x = size.width - width.toPx(), y = scrollbarOffsetY),
                size = Size(width = width.toPx(), height = scrollbarHeight),
                cornerRadius = CornerRadius(width.toPx() / 2, width.toPx() / 2)
            )
        }
    }

}