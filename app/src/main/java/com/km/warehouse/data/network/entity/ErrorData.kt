package com.km.warehouse.data.network.entity

/**
 * Create by Pustovit Oleksandr on 1/19/2026
 */
/*
* {"status":403,"message":"No primary or single unique constructor found for interface java.util.Map$Entry","error":"Forbidden","path":"uri=/api/main/insert_move_item_serial","timestamp":"2026-01-19T07:49:25.135+00:00"}
* */
data class ErrorData(
    val status: Int,
    val message: String,
    val error: String,
    val path: String = "",
    val timestamp: String = ""
){
    fun getErrorMessage(): String {
        return "$status - ${error}\n $message"
    }
}