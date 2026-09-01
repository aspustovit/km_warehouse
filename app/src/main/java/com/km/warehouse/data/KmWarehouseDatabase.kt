package com.km.warehouse.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.execSQL
import com.km.warehouse.ColumnTypes
import com.km.warehouse.data.converter.InventoryFileTypes
import com.km.warehouse.data.converter.WarehouseConverter
import com.km.warehouse.data.dao.BayerDao
import com.km.warehouse.data.dao.InventoryDao
import com.km.warehouse.data.dao.InventoryFilesDao
import com.km.warehouse.data.dao.ItemsSerialDao
import com.km.warehouse.data.dao.MoveOrderDao
import com.km.warehouse.data.dao.MoveOrderItemDao
import com.km.warehouse.data.dao.UserWarehouseDao
import com.km.warehouse.data.entity.Bayer
import com.km.warehouse.data.entity.Inventory
import com.km.warehouse.data.entity.InventoryFiles
import com.km.warehouse.data.entity.ItemsSerial
import com.km.warehouse.data.entity.MoveOrder
import com.km.warehouse.data.entity.MoveOrderItem
import com.km.warehouse.data.entity.UserWarehouse

/**
 * Create by Pustovit Oleksandr on 9/18/2025
 */
@Database(
    entities = [Bayer::class, MoveOrder::class, MoveOrderItem::class, ItemsSerial::class, InventoryFiles::class, Inventory::class,
        UserWarehouse::class],
    version = 4
)
@TypeConverters(WarehouseConverter::class)
abstract class KmWarehouseDatabase : RoomDatabase() {
    abstract fun bayerDao(): BayerDao
    abstract fun itemsSerialDao(): ItemsSerialDao
    abstract fun moveOrderDao(): MoveOrderDao
    abstract fun moveOrderItemDao(): MoveOrderItemDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun inventoryFilesDao(): InventoryFilesDao
    abstract fun userWarehouseDao(): UserWarehouseDao

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
                    ).setJournalMode(JournalMode.TRUNCATE).allowMainThreadQueries().addMigrations(
                        MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4
                    ).build()
                    INSTANCE = instance

                    return instance
                } catch (ex: Exception) {
                    return tempInstance!!
                }

            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `inventory_files` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `create_date` INTEGER NOT NULL, `file_name` TEXT NOT NULL, `comments` TEXT)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `inventory` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `file_id` INTEGER NOT NULL, `inventory_item_id` TEXT NOT NULL, `item_segment` TEXT NOT NULL, `mfg_part_number` TEXT NOT NULL, `item_description` TEXT NOT NULL, `quantity` REAL NOT NULL, `free_quantity` REAL NOT NULL, `fact_quantity` REAL, `comments` TEXT, FOREIGN KEY(`file_id`) REFERENCES `inventory_files`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_inventory_file_id` ON `inventory` (`file_id`)")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.addColumn(
                    InventoryFiles.TABLE_NAME,
                    "file_type",
                    ColumnTypes.ColumnText(),
                    false,
                    InventoryFileTypes.FULL.name
                )
            }
        }

        private val MIGRATION_3_4 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `user_warehouse` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `warehouse_type` TEXT NOT NULL, `warehouse_name` TEXT NOT NULL)")
            }
        }

        private fun SupportSQLiteDatabase.addColumn(
            tableName: String,
            columnName: String,
            columnType: ColumnTypes,
            isNullable: Boolean = false,
            stringDefValue: String = columnType.defValue
        ) {
            val sql = if (columnType.name == "TEXT")
                "ALTER TABLE $tableName ADD COLUMN $columnName ${columnType.name} ${if (isNullable) "" else "NOT NULL DEFAULT '${stringDefValue}'"}"
            else
                "ALTER TABLE $tableName ADD COLUMN $columnName ${columnType.name} ${if (isNullable) "" else "NOT NULL DEFAULT ${stringDefValue}"}"

            execSQL(sql)
        }

    }
}