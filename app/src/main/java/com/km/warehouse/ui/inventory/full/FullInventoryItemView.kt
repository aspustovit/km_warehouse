package com.km.warehouse.ui.inventory.full

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.km.warehouse.domain.usecase.inventory.InventoryFileModel
import com.km.warehouse.domain.usecase.inventory.InventoryModel
import com.km.warehouse.ui.inventory.InventoryItemView

/**
 * Create by Pustovit Oleksandr on 09/06/2026
 */
@Composable
fun FullInventoryItemView(
    fileInventoryModels: List<InventoryModel>,
    onBackClick: () -> Unit,
    onInventoryClick: (Pair<InventoryModel, Int>) -> Unit
) {
    val listState = rememberLazyListState()
    val firstVisibleIndex by remember {
        derivedStateOf { listState.firstVisibleItemIndex }
    }

    BackHandler {
        onBackClick.invoke()
    }
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .focusable(),
            state = listState
        ) {
            fileInventoryModels.forEach {
                item {
                    InventoryItemView(
                        it,
                        idx = firstVisibleIndex,
                        onInventoryClick = { inv ->
                            onInventoryClick(Pair(it, firstVisibleIndex))
                        })
                }
            }
        }
    }
}