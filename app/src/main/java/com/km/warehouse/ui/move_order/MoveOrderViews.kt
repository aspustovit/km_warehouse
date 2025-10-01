package com.km.warehouse.ui.move_order

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.km.warehouse.ui.SharedViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Create by Pustovit Oleksandr on 9/24/2025
 */

@Composable
fun MoveOrderView(
    modifier: Modifier, onWidgetClicked: () -> Unit
) {
    val viewModel: MoveOrderItemViewModel = koinViewModel()
    val state = viewModel.viewState.collectAsState()
    LaunchedEffect(viewModel) {
        viewModel.loadBayers()
    }
    Row (modifier = Modifier.fillMaxSize()){
        Text(text = state.value.bayers.size.toString())
    }
}