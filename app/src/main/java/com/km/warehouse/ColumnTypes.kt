package com.km.warehouse

/**
 * Create by Pustovit Oleksandr on 10/08/2026
 */
sealed class ColumnTypes(open val defValue: String, val name : String) {
    data class ColumnText(override val defValue: String = "") : ColumnTypes(defValue, "TEXT")
    data class ColumnInteger(override val defValue: String = "0") : ColumnTypes(defValue, "INTEGER")
    data class ColumnLong(override val defValue: String = "0") : ColumnTypes(defValue, "LONG")
    data class ColumnReal(override val defValue: String = "0") : ColumnTypes(defValue, "REAL")
    data class ColumnBlob(override val defValue: String = "null") : ColumnTypes(defValue, "BLOB")
}