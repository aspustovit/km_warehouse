package com.km.warehouse.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.km.warehouse.data.entity.Bayer

/**
 * Create by Pustovit Oleksandr on 9/23/2025
 */
@Dao
abstract class BayerDao : BaseDao<Bayer>() {
    @Query("SELECT b.* FROM bayer b where b.id = :bayerId")
    abstract fun getById(bayerId: Int): Bayer?

    @Query("SELECT b.* FROM bayer b")
    abstract fun getBayers(): List<Bayer>
}