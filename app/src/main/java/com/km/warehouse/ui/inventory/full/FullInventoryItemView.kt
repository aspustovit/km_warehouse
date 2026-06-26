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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.km.warehouse.R
import com.km.warehouse.domain.usecase.inventory.InventoryModel
import com.km.warehouse.ui.inventory.InventoryItemView
import com.km.warehouse.ui.inventory.InventoryNotFoundView

/**
 * Create by Pustovit Oleksandr on 09/06/2026
 */
@Composable
fun FullInventoryItemView(
    fileInventoryModels: List<InventoryModel>,
    onBackClick: () -> Unit,
    onInventoryClick: (Pair<InventoryModel, Int>) -> Unit,
    barcode: String?,
    setAsDoneClick: (InventoryModel) -> Unit,
    showNoInventoryMessage: Boolean
) {
    val quantityEqual = stringResource(R.string.quantity_equal)
    val listState = rememberLazyListState()
    val firstVisibleIndex by remember {
        derivedStateOf { listState.firstVisibleItemIndex }
    }

    BackHandler {
        onBackClick.invoke()
    }
    Column(modifier = Modifier.fillMaxSize()) {
        if(fileInventoryModels.isEmpty() && barcode != null && showNoInventoryMessage) {
            InventoryNotFoundView(barcode)
        } else {
            if(fileInventoryModels.isEmpty()){
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = stringResource(id = R.string.inventory_find_message),
                    fontSize = 16.sp
                )
            }
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
                            setAsDoneClick = { item ->
                                /*item.factQuantity = item.quantity
                                item.comments = quantityEqual*/
                                setAsDoneClick.invoke(item.copy(comments = quantityEqual, factQuantity = item.quantity))
                            },
                            onInventoryClick = { inv ->
                                onInventoryClick(Pair(it, firstVisibleIndex))
                            })
                    }
                }
            }
        }
    }
}