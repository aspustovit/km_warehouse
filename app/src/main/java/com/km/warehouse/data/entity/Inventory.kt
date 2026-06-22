package com.km.warehouse.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.km.warehouse.data.entity.Inventory.Companion.TABLE_NAME

/**
 * Create by Pustovit Oleksandr on 08/06/2026
 */
@Entity(tableName = TABLE_NAME, foreignKeys = [ForeignKey(
    entity = InventoryFiles::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("file_id"),
    onDelete = ForeignKey.NO_ACTION
)], indices = [Index(value = ["file_id"])])
data class Inventory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "file_id")
    val fileId: Int,
    @ColumnInfo(name = "inventory_item_id")
    val inventoryItemId: String,
    @ColumnInfo(name = "item_segment")
    val itemSegment: String,
    @ColumnInfo(name = "mfg_part_number")
    val mfgPartNumber: String,
    @ColumnInfo(name = "item_description")
    val itemDescription: String,
    val quantity: Double,
    @ColumnInfo(name = "free_quantity")
    val freeQuantity: Double,
    @ColumnInfo(name = "fact_quantity")
    val factQuantity: Double? = null,
    val comments: String? = null
) {
    companion object {
        const val TABLE_NAME = "inventory"
    }
}