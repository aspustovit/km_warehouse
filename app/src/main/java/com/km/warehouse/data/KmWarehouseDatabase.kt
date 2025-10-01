package com.km.warehouse.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.km.warehouse.data.dao.BayerDao
import com.km.warehouse.data.dao.ItemsSerialDao
import com.km.warehouse.data.dao.MoveOrderDao
import com.km.warehouse.data.dao.MoveOrderItemDao
import com.km.warehouse.data.entity.Bayer
import com.km.warehouse.data.entity.ItemsSerial
import com.km.warehouse.data.entity.MoveOrder
import com.km.warehouse.data.entity.MoveOrderItem

/**
 * Create by Pustovit Oleksandr on 9/18/2025
 */
@Database(
    entities = [Bayer::class, MoveOrder::class, MoveOrderItem::class, ItemsSerial::class],
    version = 1
)
abstract class KmWarehouseDatabase : RoomDatabase() {
    abstract fun bayerDao(): BayerDao
    abstract fun itemsSerialDao(): ItemsSerialDao
    abstract fun moveOrderDao(): MoveOrderDao
    abstract fun moveOrderItemDao(): MoveOrderItemDao

    companion object {
        const val DB_NAME = "km_warehouse_database.db"
        @Volatile
        private var INSTANCE: KmWarehouseDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): KmWarehouseDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                try {
                    val instance = Room.databaseBuilder(
                        context.applicationContext, KmWarehouseDatabase::class.java,
                        DB_NAME
                    ).setJournalMode(JournalMode.TRUNCATE).allowMainThreadQueries().build()
                    INSTANCE = instance

                    return instance
                } catch (ex: Exception) {
                    Log.e("km_warehouse_database", "${ex.printStackTrace()}")
                    return tempInstance!!
                }

            }
        }
    }
}