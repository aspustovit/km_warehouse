package com.km.warehouse.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.km.warehouse.data.entity.MoveOrderItem.Companion.TABLE_NAME

/**
 * Create by Pustovit Oleksandr on 9/19/2025
 */
@Entity(tableName = TABLE_NAME, foreignKeys = [ForeignKey(
    entity = MoveOrder::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("move_order_id"),
    onDelete = ForeignKey.NO_ACTION
)])
data class MoveOrderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val quantity: Int,
    @ColumnInfo(name = "qty_given")
    val qtyGiven: Int,
    @ColumnInfo(name = "inventory_id")
    val inventoryId: Int,
    val description: String,
    @ColumnInfo(name = "internal_code")
    val internalCode: String,
    @ColumnInfo(name = "mfr_code")
    val mfrCode: String,
    @ColumnInfo(name = "move_order_id")
    val moveOrderId: Int
) {
    companion object {
        const val TABLE_NAME = "move_order_item"
    }
}
