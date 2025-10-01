package com.km.warehouse.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery

/**
 * Create by Pustovit Oleksandr on 9/23/2025
 */
abstract class BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(items: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(item: T): Long

    @Delete
    abstract fun delete(item: T): Int

    @Update
    abstract fun update(item: T): Int

    @RawQuery
    abstract fun runQuery(query: SimpleSQLiteQuery): T

    @RawQuery
    abstract fun runQueryId(query: SimpleSQLiteQuery): Int
}