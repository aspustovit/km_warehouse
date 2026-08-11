package com.km.warehouse.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.km.warehouse.data.converter.InventoryFileTypes
import com.km.warehouse.data.entity.InventoryFiles

/**
 * Create by Pustovit Oleksandr on 08/06/2026
 */
@Dao
abstract class InventoryFilesDao : BaseDao<InventoryFiles>(){

    @Query("SELECT b.* FROM inventory_files b where file_type = :fileTypes")
    abstract fun getInventoryFiles(fileTypes: InventoryFileTypes = InventoryFileTypes.FULL): List<InventoryFiles>

    @Query("DELETE FROM inventory_files WHERE id = :fileId")
    abstract fun deleteFile(fileId: Int): Int

    @Query("UPDATE inventory_files SET file_name = :newName WHERE id = :id")
    abstract fun renameFile(id: Int, newName: String): Int

    @Query("SELECT b.* FROM inventory_files b where b.id = :fileId")
    abstract fun getInventoryFileById(fileId: Int): InventoryFiles
}