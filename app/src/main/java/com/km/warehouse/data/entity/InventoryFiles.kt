package com.km.warehouse.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.km.warehouse.data.converter.InventoryFileTypes
import com.km.warehouse.data.entity.InventoryFiles.Companion.TABLE_NAME

/**
 * Create by Pustovit Oleksandr on 08/06/2026
 */
@Entity(tableName = TABLE_NAME)
data class InventoryFiles(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "create_date")
    val createDate: Long,
    @ColumnInfo(name = "file_name")
    val fileName: String,
    val comments: String? = null,
    @ColumnInfo(name = "file_type")
    val fileType: InventoryFileTypes
) {
    companion object {
        const val TABLE_NAME = "inventory_files"
    }
}