package com.km.warehouse.data.network.entity

import com.google.gson.annotations.SerializedName

/**
 * Create by Pustovit Oleksandr on 01/06/2026
 *
 * {
 *          "inventoryItemId":405217,
 *          "itemSegment1":"PLNZZ-284",
 *          "mfgPartNumber":"486/ YL99P15.21-9 Olive green ",
 *          "itemDescription":"Деталь \"Фастекс Б 16\"\" Olive green (486/ YL99P15.21-9)",
 *          "organizationName":"Дисти",
 *          "subinvintoryCode":"ДСЛ",
 *          "quantity":3500.0,
 *          "freeQuantity":3000.0
 *       }
 */
data class InventoryEntity(
    val inventoryItemId: Long,
    @SerializedName("itemSegment1")
    val itemSegment: String,
    val mfgPartNumber: String,
    val itemDescription: String,
    val organizationName: String,
    @SerializedName("subinvintoryCode")
    val subInvintoryCode: String,
    val quantity: Double,
    val freeQuantity: Double
)
