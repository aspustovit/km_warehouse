package com.km.warehouse.data.converter

import androidx.room.TypeConverter

/**
 * Create by Pustovit Oleksandr on 10/08/2026
 */
class WarehouseConverter {
    @TypeConverter
    fun toInventoryFileTypes(value: String) = enumValueOf<InventoryFileTypes>(value)

    @TypeConverter
    fun fromInventoryFileTypes(value: InventoryFileTypes) = value.name
}