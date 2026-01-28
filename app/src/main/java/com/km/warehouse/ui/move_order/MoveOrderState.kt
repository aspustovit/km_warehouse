package com.km.warehouse.ui.move_order

import com.km.warehouse.data.entity.Bayer
import com.km.warehouse.data.network.entity.ErrorData
import com.km.warehouse.domain.usecase.model.ItemSerialModel
import com.km.warehouse.domain.usecase.model.OrderModel

/**
 * Create by Pustovit Oleksandr on 9/24/2025
 */
data class MoveOrderState(
    val bayers: List<Bayer>,
    val moveOrders: HashMap<String, List<OrderModel>> = HashMap(),
    val errorData: ErrorData? = null,
    val error: String = "",
    val itemSerials: List<ItemSerialModel> = emptyList()
)
