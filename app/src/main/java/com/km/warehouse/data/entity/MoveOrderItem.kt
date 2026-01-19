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
    val quantity: Double,
    @ColumnInfo(name = "qty_given")
    val qtyGiven: Double,
    @ColumnInfo(name = "inventory_id")
    val inventoryId: Int,
    val description: String,
    @ColumnInfo(name = "mfr_code")
    val mfrCode: String, // mfgPartNum
    @ColumnInfo(name = "move_order_id")
    val moveOrderId: Int,
    @ColumnInfo(name = "mfg_part_num_exp")
    val mfgPartNumExp: String?,
    @ColumnInfo(name = "no_serials")
    val noSerials: String,
    @ColumnInfo(name = "item_segment")
    val itemSegment: String?
) {
    companion object {
        const val TABLE_NAME = "move_order_item"
    }
}
