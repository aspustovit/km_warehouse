package com.km.warehouse.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.km.warehouse.data.entity.Bayer.Companion.TABLE_NAME

/**
 * Create by Pustovit Oleksandr on 9/18/2025
 */
@Entity(tableName = TABLE_NAME)
data class Bayer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String
) {
    companion object {
        const val TABLE_NAME = "bayer"
    }
}