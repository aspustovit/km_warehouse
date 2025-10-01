package com.km.warehouse.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.km.warehouse.data.entity.ItemsSerial.Companion.TABLE_NAME

/**
 * Create by Pustovit Oleksandr on 9/22/2025
 */
@Entity(tableName = TABLE_NAME, foreignKeys = [ForeignKey(
    entity = MoveOrderItem::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("move_order_itemId"),
    onDelete = ForeignKey.CASCADE
)])
data class ItemsSerial(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val serial: String,
    @ColumnInfo(name = "move_order_itemId")
    val moveOrderItemId: Int
){
    companion object {
        const val TABLE_NAME = "items_serial"
    }
}
