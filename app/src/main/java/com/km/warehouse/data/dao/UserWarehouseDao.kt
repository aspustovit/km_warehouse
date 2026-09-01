package com.km.warehouse.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.km.warehouse.data.entity.UserWarehouse

/**
 * Create by Pustovit Oleksandr on 26/08/2026
 */
@Dao
abstract class UserWarehouseDao : BaseDao<UserWarehouse>() {

    @Query("SELECT b.* FROM user_warehouse b")
    abstract fun getUserWarehouse(): List<UserWarehouse>

    @Query("DELETE FROM user_warehouse")
    abstract fun deleteWarehouseSettings()
}