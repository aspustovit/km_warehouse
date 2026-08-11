package com.km.warehouse.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.km.warehouse.data.entity.Inventory
import com.km.warehouse.data.entity.ItemsSerial

/**
 * Create by Pustovit Oleksandr on 08/06/2026
 */
@Dao
abstract class InventoryDao : BaseDao<Inventory>() {
    @Query("SELECT b.* FROM inventory b where b.file_id == :fileId and b.mfg_part_number like '%' || :mfgPartNumber || '%'")
    abstract fun getInventoryByFile(fileId: Int, mfgPartNumber: String): List<Inventory>

    @Query("SELECT b.* FROM inventory b where b.file_id == :fileId and b.mfg_part_number = ''")
    abstract fun getEmptyBarcodes(fileId: Int): List<Inventory>

    @Query("DELETE FROM inventory WHERE file_id = :fileId")
    abstract fun deleteInventoryByFile(fileId: Int): Int

    @Query("SELECT b.* FROM inventory b where b.file_id == :fileId")
    abstract fun exportInventoryByFile(fileId: Int): List<Inventory>

    @Query("SELECT b.* FROM inventory b where b.file_id == :fileId and b.inventory_item_id = :mfgPartNumber")
    abstract fun getInventoryByItemId(fileId: Int, mfgPartNumber: String): List<Inventory>

    @Query("SELECT b.* FROM inventory b where b.file_id == :fileId and b.item_segment = :itemSegment")
    abstract fun getInventoryByItemSegment(fileId: Int, itemSegment: String): List<Inventory>

    @Query("SELECT b.* FROM inventory b where b.file_id == :fileId and b.inventory_item_id = :inventoryItemId")
    abstract fun getInventoryByInventoryItem(fileId: Int, inventoryItemId: String): List<Inventory>

    @Query("SELECT b.* FROM inventory b where b.file_id == :fileId")
    abstract fun getPartInventoryByFile(fileId: Int): List<Inventory>
}