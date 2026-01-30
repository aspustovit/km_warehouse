package com.km.warehouse.domain.repository

import com.km.warehouse.domain.usecase.model.BayerModel
import com.km.warehouse.domain.usecase.model.ItemSerialModel
import com.km.warehouse.domain.usecase.model.MoveOrderItemsModel
import com.km.warehouse.domain.usecase.model.MoveOrderModel
import com.km.warehouse.domain.usecase.model.SyncResultModel

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
/**
 *
 *   @PostMapping("/set_move_order_status")
 *   public ResponseEntity<?> setMoveOrderStatus(long moveOrderId, String status, String isComplete)
 *
 *   @GetMapping("/move_order_item_serial")
 *   public ResponseEntity<?>  getMoveOrderItemSerial(long moveOrderItemId, String serialNumber)
 *
 *   @GetMapping("/move_order_item_serial_by_id")
 *   public ResponseEntity<?>  getMoveOrderItemSerial(long moveOrderItemId)
 *
 *   @GetMapping("/move_order_item_serial_by_sn")
 *   public ResponseEntity<?>  getMoveOrderItemSerial(String serialNumber)
 *
 *   @GetMapping("/move_order_item_serial_by_is")
 *   public ResponseEntity<?>  getMoveOrderItemSerialByItemSegment(String itemSegment1)
 *
 *   @PostMapping("/insert_move_item_serial")
 *   public ResponseEntity<?> insertMoveOrderItemSerial(long moveOrderItemId, String serialNumber)
 *
 *   @PostMapping("/delete_move_item_serial")
 *   public ResponseEntity<?> deleteMoveOrderItemSerial(long moveOrderItemId, String serialNumber)
 * */
interface SyncWarehouseRepository {
    suspend fun syncWarehouseData(): SyncResultModel

    suspend fun syncToServerSerialsData(): SyncResultModel

    suspend fun getDocumentCountForSync(): Int

    /*suspend fun loadBayer(): List<BayerModel>

    suspend fun loadMoveOrders(): List<MoveOrderModel>

    suspend fun loadMoveOrder(moveOrderId: Int): MoveOrderModel

    suspend fun loadMoveOrderItems(moveOrderId: Int): List<MoveOrderItemsModel>

    suspend fun loadMoveOrderItem(moveOrderItemId: Int): MoveOrderItemsModel

    suspend fun loadMoveOrderItemSerial(moveOrderItemId: Int): ItemSerialModel*/
}