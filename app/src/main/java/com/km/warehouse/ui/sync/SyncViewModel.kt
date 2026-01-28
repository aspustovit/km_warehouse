package com.km.warehouse.ui.sync

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.km.warehouse.domain.usecase.SyncToServerSerialsUseCase
import com.km.warehouse.domain.usecase.SyncWarehouseDataUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
class SyncViewModel(
    private val syncWarehouseDataUseCase: SyncWarehouseDataUseCase,
    private val syncToServerSerialsUseCase: SyncToServerSerialsUseCase
) : ViewModel() {
    private var _viewState: MutableStateFlow<SyncState> = MutableStateFlow(
        SyncState(syncStatus = SyncStatus.NOT_STARTED)
    )
    val viewState: StateFlow<SyncState> = _viewState

    fun initState() {
        _viewState.update { state -> state.copy(syncStatus = SyncStatus.NOT_STARTED, syncError = null) }
    }

    fun runSync() {
        Log.v("syncWarehouseData_S", "START")
        _viewState.update { state -> state.copy(syncStatus = SyncStatus.STARTED, syncError = null) }
        viewModelScope.launch {
            syncWarehouseDataUseCase.invoke(Unit).onSuccess {
                Log.i("syncWarehouseData_", "$it")
                if (it.isSyncSuccess) {
                    _viewState.update { state -> state.copy(syncStatus = SyncStatus.FINISHED) }
                } else {
                    _viewState.update { state ->
                        state.copy(
                            syncStatus = SyncStatus.ERROR,
                            syncError = if (it.errorData != null) it.errorData.getErrorMessage() else ""
                        )
                    }
                }
            }.onFailure {
                Log.e("syncWarehouseData_E", "$it")
                _viewState.update { state ->
                    state.copy(
                        syncStatus = SyncStatus.ERROR,
                        syncError = it.message
                    )
                }
            }
        }
    }

    fun runSyncToServer() {
        _viewState.update { state -> state.copy(syncStatus = SyncStatus.STARTED, syncError = null) }
        viewModelScope.launch {
            delay(1000L)
            syncToServerSerialsUseCase.invoke(Unit).onSuccess {
                if (it.isSyncSuccess) {
                    _viewState.update { state -> state.copy(syncStatus = SyncStatus.FINISHED) }
                } else {
                    _viewState.update { state ->
                        state.copy(
                            syncStatus = SyncStatus.ERROR,
                            syncError = it.errorData?.getErrorMessage()
                        )
                    }
                }
            }.onFailure {
                _viewState.update { state ->
                    state.copy(
                        syncStatus = SyncStatus.ERROR,
                        syncError = it.message
                    )
                }
            }
        }
    }
}