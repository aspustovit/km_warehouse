package com.km.warehouse.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.km.warehouse.data.entity.MoveOrder.Companion.TABLE_NAME

/**
 * Create by Pustovit Oleksandr on 9/19/2025
 */
@Entity(tableName = TABLE_NAME, foreignKeys = [ForeignKey(
    entity = Bayer::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("bayer_id"),
    onDelete = ForeignKey.NO_ACTION
)])
data class MoveOrder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    @ColumnInfo(name = "creation_date")
    val creationDate: String,
    @ColumnInfo(name = "move_date")
    val moveDate: String,
    val number: String,
    @ColumnInfo(name = "bayer_id")
    val bayerId: Int,
    val status: String,
    @ColumnInfo(name = "scanner_id")
    val scannerId: Int,
    @ColumnInfo(name = "is_complete")
    val isComplete: String
) {
    companion object {
        const val TABLE_NAME = "move_order"
    }
}
