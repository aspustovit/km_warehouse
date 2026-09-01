package com.km.warehouse.ui.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.km.warehouse.data.preference.KmWarehousePreference
import com.km.warehouse.data.preference.KmWarehousePreference.SERCH_BY_MFR_CODE
import com.km.warehouse.data.preference.KmWarehousePreference.TERMINAL_ID
import com.km.warehouse.domain.usecase.inventory.UserWarehouseModel
import com.km.warehouse.domain.usecase.settings.GetUserWarehouseSettingsUseCase
import com.km.warehouse.domain.usecase.settings.SetUserWarehouseSettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableMap

/**
 * Create by Pustovit Oleksandr on 30/03/2026
 */
class SettingsViewModel(
    val context: Context,
    val getUserWarehouseSettingsUseCase: GetUserWarehouseSettingsUseCase,
    val setUserWarehouseSettingsUseCase: SetUserWarehouseSettingsUseCase
) : ViewModel() {
    private var _settingsState: MutableStateFlow<SettingsState> =
        MutableStateFlow(SettingsState(terminalId = ""))
    val viewState: StateFlow<SettingsState> = _settingsState

    fun loadUserSettings() {
        viewModelScope.launch {
            getUserWarehouseSettingsUseCase.invoke(Unit).onSuccess { warehouses ->
                _settingsState.update {
                    it.copy(
                        terminalId = getPreferences().getString(TERMINAL_ID, "")!!,
                        searchByMfrCode = getPreferences().getBoolean(SERCH_BY_MFR_CODE, true),
                        userWarehouseModel = warehouses
                    )
                }
            }
        }
    }

    fun saveSettings(terminalId: String) {
        viewModelScope.launch {
            val warehouses = _settingsState.value.userWarehouseModel
            val warehousesList = ArrayList<UserWarehouseModel>()
            warehouses.forEach { string, models ->
                warehousesList.addAll(models)
            }
            setUserWarehouseSettingsUseCase.invoke(warehousesList).onSuccess {
                getPreferences().edit {
                    putString(TERMINAL_ID, terminalId)
                }
            }
        }
    }

    private fun getPreferences(): SharedPreferences {
        return context.getSharedPreferences(KmWarehousePreference.TOKENS_PREF, Context.MODE_PRIVATE)
    }

    fun selectWarehouse(userWarehouseModel: UserWarehouseModel, isSelected: Boolean) {
        val warehouses = _settingsState.value.userWarehouseModel.toMutableMap()
        var newWarehouseList: List<UserWarehouseModel>? = null
        var key: String? = null
        warehouses.forEach { string, models ->
            var warehouse: UserWarehouseModel? = null
            var warehouseIndex: Int = 0

            models.forEachIndexed { index, model ->
                if (model.warehouseName == userWarehouseModel.warehouseName) {
                    warehouse = model
                    warehouseIndex = index
                }
            }
            if (warehouse != null) {
                val newList = models.toMutableList()
                newList.remove(warehouse)
                newList.add(warehouseIndex, warehouse.copy(isSelected = isSelected))
                newWarehouseList = newList
                key = string
                return@forEach
            }
        }
        if(newWarehouseList != null){
            warehouses[key!!] = newWarehouseList
            _settingsState.update {
                it.copy(
                    userWarehouseModel = warehouses.toImmutableMap()
                )
            }
        }
    }
}