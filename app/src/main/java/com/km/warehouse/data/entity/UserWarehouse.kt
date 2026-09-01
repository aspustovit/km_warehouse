package com.km.warehouse.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.km.warehouse.data.entity.UserWarehouse.Companion.TABLE_NAME

/**
 * Create by Pustovit Oleksandr on 26/08/2026
 */
@Entity(tableName = TABLE_NAME)
data class UserWarehouse(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "warehouse_type")
    val warehouseType: String,
    @ColumnInfo(name = "warehouse_name")
    val warehouseName: String
){
    companion object {
        const val TABLE_NAME = "user_warehouse"
    }
}
