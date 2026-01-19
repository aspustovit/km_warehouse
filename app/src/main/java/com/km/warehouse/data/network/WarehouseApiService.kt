package com.km.warehouse.data.network

import com.km.warehouse.data.network.entity.BaseResponse
import com.km.warehouse.data.network.entity.BayerEntity
import com.km.warehouse.data.network.entity.ItemSerialEntity
import com.km.warehouse.data.network.entity.MoveOrderEntity
import com.km.warehouse.data.network.entity.MoveOrderItemsEntity
import com.km.warehouse.domain.usecase.model.MoveOrderModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */

/**
 *
 * @GetMapping("/buyers")
 *   public ResponseEntity<?> getBuyers()
 *
 *   @GetMapping("/move_order")
 *   public ResponseEntity<?> getMoveOrder(long moveOrderId)
 *
 *   @GetMapping("/move_orders")
 *   public ResponseEntity<?> getMoveOrders()
 *
 *   @PostMapping("/set_move_order_status")
 *   public ResponseEntity<?> setMoveOrderStatus(long moveOrderId, String status, String isComplete)
 *
 *   @GetMapping("/move_order_item")
 *   public ResponseEntity<?>  getMoveOrderItem(long moveOrderItemId)   }
 *
 *   @GetMapping("/move_order_items")
 *   public ResponseEntity<?>  getMoveOrderItems(long moveOrderId) throws
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
interface WarehouseApiService {
    @Headers("Content-Type: application/json")
    @GET("/api/main/buyers")
    fun getBuyers(): Call<BaseResponse<HashMap<String, String>>>

    @Headers("Content-Type: application/json")
    @GET("/api/main/move_order")
    fun getMoveOrder(@Query("moveOrderId") moveOrderId: Int): Call<BaseResponse<MoveOrderEntity>>

    @Headers("Content-Type: application/json")
    @GET("/api/main/move_orders")
    fun getMoveOrders(): Call<BaseResponse<List<MoveOrderEntity>>>

    @Headers("Content-Type: application/json")
    @GET("/api/main/move_order_items")
    fun getMoveOrderItems(@Query("moveOrderId") moveOrderId: Int): Call<BaseResponse<List<MoveOrderItemsEntity>>>

    @Headers("Content-Type: application/json")
    @GET("/api/main/move_order_item_serial_by_id")
    fun getMoveOrderItemSerial(@Query("moveOrderItemId") moveOrderItemId: Int): Call<BaseResponse<List<ItemSerialEntity>>>

    /*
    * @PostMapping("/insert_move_item_serials")
  public ResponseEntity<?> insertMoveOrderItemSerial(List<Entry<Long, String>> moveOrderItemSerials) throws Throwable {
    mainRepository.insertMoveOrderItemSerials(moveOrderItemSerials);
    return ResponseEntity.ok(new SuccessResponse<String>(null, "OK"));
  }
    * */
    @FormUrlEncoded
    @POST("/api/main/insert_move_item_serial")
    fun insertMoveOrderItemSerial(
        @Field("moveOrderItemSerials") moveOrderItemSerials: HashMap<Long, String>
    ): Call<BaseResponse<Unit>>
}