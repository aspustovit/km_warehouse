package com.km.warehouse.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.km.warehouse.data.entity.Bayer
import com.km.warehouse.data.entity.MoveOrder

/**
 * Create by Pustovit Oleksandr on 9/23/2025
 */
@Dao
abstract class MoveOrderDao: BaseDao<MoveOrder>() {
    @Query("SELECT b.* FROM move_order b WHERE b.is_complete = :code")
    abstract fun getMoveOrders(code: String): List<MoveOrder>

    @Query("SELECT b.* FROM move_order b")
    abstract fun getMoveOrders(): List<MoveOrder>
}