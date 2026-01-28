package com.km.warehouse.data.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.km.warehouse.data.KmWarehouseDatabase
import com.km.warehouse.data.entity.Bayer
import com.km.warehouse.data.network.WarehouseApiService
import com.km.warehouse.data.network.entity.ErrorData
import com.km.warehouse.domain.repository.SyncWarehouseRepository
import com.km.warehouse.domain.usecase.model.SyncResultModel


/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
class SyncWarehouseRepositoryImpl(
    private val warehouseApiService: WarehouseApiService,
    private val database: KmWarehouseDatabase
) :
    SyncWarehouseRepository {

    override suspend fun syncWarehouseData(): SyncResultModel {
        try {
            val response = warehouseApiService.getBuyers().execute()
            var errorData : ErrorData? = null
            if(!response.isSuccessful)
                errorData = parseError(response.errorBody()!!.string())
            val data = response.body()?.data
            data?.apply {
                forEach {
                    val bayer = Bayer(id = it.key.toInt(), description = it.value)
                    val id = database.bayerDao().insert(bayer)
                    Log.d("syncWarehouseData", "BuyerID = $id")
                }
            }
            val moveOrderResponse = warehouseApiService.getMoveOrders().execute()
            if(!moveOrderResponse.isSuccessful)
                errorData = parseError(moveOrderResponse.errorBody()!!.string())
            /*    val win1251Bytes =
                        moveOrder.moveOrderModel.description.toByteArray(Charset.forName("Windows-1251"))
                    val s = String(win1251Bytes, StandardCharsets.UTF_8)
                    */
            Log.v("_syncWarehouseData", "${moveOrderResponse.body()}")
            val moveOrderData = moveOrderResponse.body()?.data
            moveOrderData?.apply {
                forEach {
                    val bayer = database.bayerDao().getById(it.bayerId)

                    if (bayer == null) {
                        database.bayerDao()
                            .insert(Bayer(id = it.bayerId, description = it.bayerName))
                    }
                    val id = database.moveOrderDao().insert(it.toMoveOrderDb())
                    if (id != 0L) {
                        errorData = syncMoveOrderItems(id)
                    }
                }
            }

            Log.d("syncWarehouseData", "Buyers = $data")
            return SyncResultModel(isSyncSuccess = response.isSuccessful, errorData = errorData)
        } catch (ex: Exception) {
            Log.e("syncWarehouseData", "$ex")
            return SyncResultModel(isSyncSuccess = false, errorData = ErrorData(status = 600, message = ex.toString(), error = "syncWarehouseData") )
        }

    }

    override suspend fun syncToServerSerialsData(): SyncResultModel {
        val serials = database.itemsSerialDao().getSerialsToSync()
        try {

            val listToSend = serials.map { it.toItemSerialSync() }
            if (listToSend.size == 1) {
                val first = listToSend.first()
                val response = warehouseApiService.insertMoveOrderItemSerial(first).execute()
                Log.d("syncToServerWarehouseData_F", "${response.raw()}")
                if (response.errorBody() != null) {
                    val errorData = parseError(response.errorBody()!!.string())
                    return SyncResultModel(
                        isSyncSuccess = response.isSuccessful,
                        errorData = errorData,
                    )
                } else {
                    return SyncResultModel(
                        isSyncSuccess = response.isSuccessful,
                        errorData = null,
                    )
                }
            } else {
                Log.d("syncToServerWarehouseData", "${listToSend}")
                val response = warehouseApiService.insertMoveOrderItemSerials(listToSend).execute()
                Log.d("syncToServerWarehouseData", "${response.raw()}")
                if (response.errorBody() != null) {
                    val errorData = parseError(response.errorBody()!!.string())
                    return SyncResultModel(
                        isSyncSuccess = response.isSuccessful,
                        errorData = errorData,
                    )
                } else {
                    return SyncResultModel(
                        isSyncSuccess = response.isSuccessful,
                        errorData = null,
                    )
                }
            }

        } catch (ex: Exception) {
            Log.e("syncToServerWarehouseData", "$ex")
            return SyncResultModel(isSyncSuccess = false,  errorData = ErrorData(status = 600, message = ex.toString(), error = "syncToServerWarehouseData") )
        }
    }

    private fun syncMoveOrderItems(moveOrderId: Long): ErrorData?{
        val response =
            warehouseApiService.getMoveOrderItems(moveOrderId = moveOrderId.toInt()).execute()
        var errorData : ErrorData? = null
        if(!response.isSuccessful)
            errorData = parseError(response.errorBody()!!.string())
        val data = response.body()?.data
        data?.apply {
            forEach {
                val id = database.moveOrderItemDao().insert(it.toMoveOrderItemDb())
                Log.d("syncWarehouseData", "MoveOrderItem = $id")
            }
        }
        return errorData
    }

    fun parseError(errorBody: String): ErrorData? {
        val gson = Gson()
        val error = gson.fromJson(errorBody, ErrorData::class.java)
        Log.e("syncToServerWarehouseData", "$error")
        return error

        /*Log.e("syncToServerWarehouseData", "$errorBody")
        val errorMessage = JsonParser().parse(errorBody)
            .asJsonObject["error"]
            .asJsonObject["message"]
            .asString
        Log.e("syncToServerWarehouseData", "$errorMessage")*/
    }

    /*override suspend fun loadBayer(): List<BayerModel> {
        TODO("Not yet implemented")
    }

    override suspend fun loadMoveOrders(): List<MoveOrderModel> {
        TODO("Not yet implemented")
    }

    override suspend fun loadMoveOrder(moveOrderId: Int): MoveOrderModel {
        TODO("Not yet implemented")
    }

    override suspend fun loadMoveOrderItems(moveOrderId: Int): List<MoveOrderItemsModel> {
        TODO("Not yet implemented")
    }

    override suspend fun loadMoveOrderItem(moveOrderItemId: Int): MoveOrderItemsModel {
        TODO("Not yet implemented")
    }

    override suspend fun loadMoveOrderItemSerial(moveOrderItemId: Int): ItemSerialModel {
        TODO("Not yet implemented")
    }*/
}