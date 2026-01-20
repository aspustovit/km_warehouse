package com.km.warehouse.data.repository

import android.util.Log
import com.km.warehouse.data.KmWarehouseDatabase
import com.km.warehouse.data.entity.Bayer
import com.km.warehouse.data.network.WarehouseApiService
import com.km.warehouse.domain.repository.SyncWarehouseRepository
import com.km.warehouse.domain.usecase.model.BayerModel
import com.km.warehouse.domain.usecase.model.ItemSerialModel
import com.km.warehouse.domain.usecase.model.MoveOrderItemsModel
import com.km.warehouse.domain.usecase.model.MoveOrderModel
import com.km.warehouse.domain.usecase.model.SyncResultModel

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
class SyncWarehouseRepositoryImpl(private val warehouseApiService: WarehouseApiService,private val database: KmWarehouseDatabase) :
    SyncWarehouseRepository {

    override suspend fun syncWarehouseData(): SyncResultModel {
        try {
            val response = warehouseApiService.getBuyers().execute()
            val data = response.body()?.data
            data?.apply {
                forEach {
                    val bayer = Bayer(id = it.key.toInt(), description = it.value)
                    val id = database.bayerDao().insert(bayer)
                    Log.d("syncWarehouseData", "BuyerID = $id")
                }
            }
            val moveOrderResponse = warehouseApiService.getMoveOrders().execute()
            val moveOrderData = moveOrderResponse.body()?.data
            moveOrderData?.apply {
                forEach {
                    val bayer = database.bayerDao().getById(it.bayerId)
                    Log.v("_syncWarehouseData", "${it}")
                    Log.v("_syncWarehouseData", "${it.bayerId} = $bayer")
                    if(bayer == null) {
                        database.bayerDao().insert(Bayer(id= it.bayerId, description = it.bayerName))
                    }
                    val id = database.moveOrderDao().insert(it.toMoveOrderDb())
                    if(id != 0L) {
                        syncMoveOrderItems(id)
                    }
                }
            }

            Log.d("syncWarehouseData", "Buyers = $data")
            return SyncResultModel(isSyncSuccess = response.isSuccessful)
        }catch (ex: Exception){
            Log.e("syncWarehouseData", "$ex")
            return SyncResultModel(isSyncSuccess = false, errorMessage = ex.toString())
        }

    }

    override suspend fun syncToServerSerialsData(): SyncResultModel {
        val serials = database.itemsSerialDao().getSerialsToSync()
        try {

            val listToSend = serials.map { it.toItemSerialSync() }
            Log.d("syncToServerWarehouseData", "${listToSend}")
            val response = warehouseApiService.insertMoveOrderItemSerial(listToSend).execute()
            Log.d("syncToServerWarehouseData", "${response.raw()}")
            return SyncResultModel(isSyncSuccess = response.isSuccessful, "${response.code()}\n${response.body()?.error}", errorCode = response.code())
        } catch (ex: Exception){
            Log.e("syncToServerWarehouseData", "$ex")
            return SyncResultModel(isSyncSuccess = false, "$ex", errorCode = 500)
        }
    }

    private fun syncMoveOrderItems(moveOrderId: Long) {
        val response = warehouseApiService.getMoveOrderItems(moveOrderId = moveOrderId.toInt()).execute()
        val data = response.body()?.data
        data?.apply {
            forEach {
                val id = database.moveOrderItemDao().insert(it.toMoveOrderItemDb())
                Log.d("syncWarehouseData", "MoveOrderItem = $id")
            }
        }
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