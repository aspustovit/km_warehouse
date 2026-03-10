package com.km.warehouse.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.km.warehouse.data.entity.Bayer
import com.km.warehouse.data.entity.ItemsSerial

/**
 * Create by Pustovit Oleksandr on 9/23/2025
 */
@Dao
abstract class ItemsSerialDao : BaseDao<ItemsSerial>() {
    @Query("SELECT b.* FROM items_serial b where b.is_synced == 0")
    abstract fun getSerialsToSync(): List<ItemsSerial>

    @Query("DELETE FROM items_serial WHERE serial = :serial")
    abstract fun deleteBySerial(serial: String): Int

    @Query("SELECT b.* FROM items_serial b where b.serial == :serial ")
    abstract fun checkSerialAlreadyAdded(serial: String): ItemsSerial?

    @Query("UPDATE items_serial SET is_synced = 1 WHERE id = :id")
    abstract fun setSynced(id: Int): Int
}