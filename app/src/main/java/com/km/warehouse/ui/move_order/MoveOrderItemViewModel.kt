package com.km.warehouse.ui.move_order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.km.warehouse.domain.usecase.LoadBayerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Create by Pustovit Oleksandr on 9/24/2025
 */
class MoveOrderItemViewModel(private val loadBayerUseCase: LoadBayerUseCase) : ViewModel() {

    private var _viewState: MutableStateFlow<MoveOrderState> = MutableStateFlow(
        MoveOrderState(bayers = emptyList())
    )
    val viewState: StateFlow<MoveOrderState> = _viewState

    fun loadBayers() {
        viewModelScope.launch {
            loadBayerUseCase.invoke(Unit).onSuccess { result ->
                _viewState.update { it.copy(bayers = result) }
            }.onFailure {
                Log.e("loadBayerUseCase", "$it")
            }
        }
    }
}