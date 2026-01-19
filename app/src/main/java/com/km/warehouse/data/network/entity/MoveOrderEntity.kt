package com.km.warehouse.data.network.entity

import com.google.gson.annotations.SerializedName

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
/**
 * {
 *             "moveId": 7474763,
 *             "creationDate": "2007-12-28T17:01:51",
 *             "moveNumber": "SG/T515T3475 /3",
 *             "moveDate": "2007-12-28",
 *             "description": "%C2%FB%E4%E0%F7%E0.+%CA%EE%ED%F2%F0%E0%EA%F2+%B9T515T3475++%ED%E0+%E4%E0%F2%F3+05.06.2007",
 *             "status": "G",
 *             "buyerId": 5380,
 *             "buyer_name": "%22%C7%C0%C7%22+%C7%C0%CE",
 *             "scannerId": 1,
 *             "isComplete": "Y"
 *         }
 * */
data class MoveOrderEntity(
    @SerializedName("moveId")
    override val id: Int,
    val description: String,
    val creationDate: String,
    val moveDate: String,
    @SerializedName("moveNumber")
    val number: String,
    @SerializedName("buyerId")
    val bayerId: Int,
    val status: String,
    val scannerId: Int,
    val isComplete: String,
    @SerializedName("buyer_name")
    val bayerName: String
) : ResponseEntity(id)
