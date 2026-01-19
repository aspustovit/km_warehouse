package com.km.warehouse.data.network.entity

import com.google.gson.annotations.SerializedName

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
/**
 *      {
 *             "moveItemId": 18564577,
 *             "moveId": 7474763,
 *             "quantity": 1.0,
 *             "quantityGived": 1.0,
 *             "inventoryItemId": 183543,
 *             "itemDescription": "Сервер HP ProLiant DL365: 2xAMD Opteron 2214HE (DC/2.2GHz/2x1MB), 7GB Reg PC2-5300, Smart Array P400i/512MB, Internal SAS Cable, 6x72.8GB SAS SFF 10K 2,5\", CD-RW/DVD-ROM Combo, dual Gigabit Ethernet, SLES x86 32/64bit 2P 1Y Sub No Media SW",
 *             "itemSegment1": "SRVHP-10435/G",
 *             "mfgPartNum": "411359-421\\splavka\\ZAZ",
 *             "noSerials": "N",
 *             "mfgPartNumExp": null
 *         },
 * */
data class MoveOrderItemsEntity(
    @SerializedName("moveItemId")
    override val id: Int,
    @SerializedName("moveId")
    val moveOrderId: Int,
    val quantity: Double,
    @SerializedName("quantityGived")
    val qtyGiven: Double,
    @SerializedName("inventoryItemId")
    val inventoryId: Int,
    @SerializedName("itemDescription")
    val description: String,
    val itemSegment1: String,
    val mfgPartNum: String,
    val noSerials: String,
    val mfgPartNumExp: String?
) : ResponseEntity(id)