package com.km.warehouse.ui.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.km.warehouse.data.preference.KmWarehousePreference
import com.km.warehouse.data.preference.KmWarehousePreference.TERMINAL_ID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Create by Pustovit Oleksandr on 30/03/2026
 */
class SettingsViewModel(val context: Context): ViewModel() {
    private var _settingsState: MutableStateFlow<SettingsState> =
        MutableStateFlow(SettingsState(terminalId = ""))
    val viewState: StateFlow<SettingsState> = _settingsState

    fun loadTerminalId() {
        viewModelScope.launch {
            _settingsState.update {
                it.copy(terminalId = getPreferences().getString(TERMINAL_ID, "")!!)
            }
        }
    }

    fun putTerminalId(terminalId: String) {
        return getPreferences().edit {
            putString(TERMINAL_ID, terminalId)
        }
    }

    private fun getPreferences(): SharedPreferences {
        return context.getSharedPreferences(KmWarehousePreference.TOKENS_PREF, Context.MODE_PRIVATE)
    }
}