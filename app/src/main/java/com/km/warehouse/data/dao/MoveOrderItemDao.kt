package com.km.warehouse.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.km.warehouse.data.entity.MoveOrder
import com.km.warehouse.data.entity.MoveOrderItem

/**
 * Create by Pustovit Oleksandr on 9/23/2025
 */
@Dao
abstract class MoveOrderItemDao : BaseDao<MoveOrderItem>() {
    @Query("SELECT m.* FROM move_order_item m where m.move_order_id = :moveOrderId")
    abstract fun getMoveOrderItemsByOrderId(moveOrderId: Int): List<MoveOrderItem>

    @Query("DELETE FROM move_order_item  where id = :moveOrderItemId")
    abstract fun deleteById(moveOrderItemId: Int): Int
}