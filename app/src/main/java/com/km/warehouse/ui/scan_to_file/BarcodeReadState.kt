package com.km.warehouse.ui.scan_to_file

/**
 * Create by Pustovit Oleksandr on 9/26/2025
 */
data class BarcodeReadState(
    val lastBarcode: String,
    val barcodeData: String = "",
    val errorMessage: String = ""
)