package com.km.warehouse.data.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.km.warehouse.data.KmWarehouseDatabase
import com.km.warehouse.data.entity.Bayer
import com.km.warehouse.data.entity.MoveOrderItem
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
            var errorData: ErrorData? = null
            if (!response.isSuccessful)
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
            if (!moveOrderResponse.isSuccessful)
                errorData = parseError(moveOrderResponse.errorBody()!!.string())
            val moveOrderData = moveOrderResponse.body()?.data
            moveOrderData?.apply {
                forEach {
                    val bayer = database.bayerDao().getById(it.bayerId)

                    if (bayer == null) {
                        database.bayerDao()
                            .insert(Bayer(id = it.bayerId, description = it.bayerName))
                    }
                    Log.v("_syncWarehouseData", "${it.toMoveOrderDb()}")
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
            return SyncResultModel(
                isSyncSuccess = false,
                errorData = ErrorData(
                    status = 600,
                    message = ex.toString(),
                    error = "syncWarehouseData"
                )
            )
        }

    }

    override suspend fun syncToServerSerialsData(): SyncResultModel {
        val serials = database.itemsSerialDao().getSerialsToSync()
        val moveOrderItems = database.moveOrderItemDao().getMoveOrderItems()
        val moveOrderItemsNoSerials = database.moveOrderItemDao().getMoveOrderItemsNoSerials()
        val moveOrderWidthSerials = HashSet<Int>()
        serials.forEach {
            moveOrderWidthSerials.add(it.moveOrderItemId)
        }
        moveOrderItemsNoSerials.forEach { moveOrderWidthSerials.add(it.id) }
        try {
            val listToSend = serials.map { it.toItemSerialSync() }
            val moveOrdersItemToDel = ArrayList<MoveOrderItem>()
            Log.i("syncToServerWarehouseData", "${moveOrderWidthSerials}")
            moveOrderWidthSerials.forEach {
                val moveOrderItem = moveOrderItems.find { moi -> moi.id == it }
                if (moveOrderItem != null) {
                    val sync = moveOrderItem.toMoveOrderItemSync()
                    val upadateResponce = warehouseApiService.updateMoveOrderItem(sync).execute()
                    Log.i("syncToServerWarehouseData", "${sync}")
                    if (upadateResponce.errorBody() != null) {
                        val errorData = parseError(upadateResponce.errorBody()!!.string())
                        return SyncResultModel(
                            isSyncSuccess = upadateResponce.isSuccessful,
                            errorData = errorData,
                        )
                    } else {
                        moveOrdersItemToDel.add(moveOrderItem)
                    }
                }
            }

            val response = warehouseApiService.insertMoveOrderItemSerials(listToSend).execute()
            Log.d("syncToServerWarehouseData", "${response.raw()}")
            if (response.errorBody() != null) {
                val errorData = parseError(response.errorBody()!!.string())
                return SyncResultModel(
                    isSyncSuccess = response.isSuccessful,
                    errorData = errorData,
                )
            } else {
                serials.forEach {
                    database.itemsSerialDao().delete(it)
                    database.moveOrderItemDao().deleteById(it.moveOrderItemId)
                }
                moveOrdersItemToDel.forEach {
                    database.moveOrderItemDao().delete(it)
                }

                return SyncResultModel(
                    isSyncSuccess = response.isSuccessful,
                    errorData = null,
                )
            }
        } catch (ex: Exception) {
            Log.e("syncToServerWarehouseData", "$ex")
            return SyncResultModel(
                isSyncSuccess = false,
                errorData = ErrorData(
                    status = 600,
                    message = ex.toString(),
                    error = "syncToServerWarehouseData"
                )
            )
        }
    }

    private fun syncMoveOrderItems(moveOrderId: Long): ErrorData? {
        val response =
            warehouseApiService.getMoveOrderItems(moveOrderId = moveOrderId.toInt()).execute()
        var errorData: ErrorData? = null
        if (!response.isSuccessful)
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

    private fun parseError(errorBody: String): ErrorData? {
        val gson = Gson()
        val error = gson.fromJson(errorBody, ErrorData::class.java)
        Log.e("syncToServerWarehouseData", "$error")
        return error
    }

    override suspend fun getDocumentCountForSync(): Int {
        return database.itemsSerialDao().getSerialsToSync().size
    }
}