package com.km.warehouse.domain

import com.km.warehouse.domain.usecase.CheckInputSerialsUseCase
import com.km.warehouse.domain.usecase.DeleteInventoryFileUseCase
import com.km.warehouse.domain.usecase.DeleteMoveOrderUseCase
import com.km.warehouse.domain.usecase.DeleteSerialNumberUseCase
import com.km.warehouse.domain.usecase.ExportFullInventoryUseCase
import com.km.warehouse.domain.usecase.GetDocumentDataToSyncUseCase
import com.km.warehouse.domain.usecase.GetInventoryByFileUseCase
import com.km.warehouse.domain.usecase.GetInventoryFilesUseCase
import com.km.warehouse.domain.usecase.GetInventorySegmentUseCase
import com.km.warehouse.domain.usecase.GetItemSerialFromDBUseCase
import com.km.warehouse.domain.usecase.LoadBayerUseCase
import com.km.warehouse.domain.usecase.LoadMoveOrdersUseCase
import com.km.warehouse.domain.usecase.auth.LoginUseCase
import com.km.warehouse.domain.usecase.ObserveBarcodeDataUseCase
import com.km.warehouse.domain.usecase.RenameInventoryFileUseCase
import com.km.warehouse.domain.usecase.SaveSerialToDBUseCase
import com.km.warehouse.domain.usecase.SetNoSerialsUseCase
import com.km.warehouse.domain.usecase.SetQuantityGivenUseCase
import com.km.warehouse.domain.usecase.SyncToServerSerialsUseCase
import com.km.warehouse.domain.usecase.SyncWarehouseDataUseCase
import com.km.warehouse.domain.usecase.UpdateFullInventoryModelUseCase
import com.km.warehouse.domain.usecase.UpdateSerialNumberUseCase
import com.km.warehouse.domain.usecase.UploadInventoryExelFileUseCase
import com.km.warehouse.domain.usecase.auth.GetPrevLoginUseCase
import com.km.warehouse.domain.usecase.auth.LogoutUseCase
import org.koin.dsl.module

/**
 * Create by Pustovit Oleksandr on 9/24/2025
 */
object DomainKoinModule {
    private val databaseModule = module {
        factory { LoadBayerUseCase(bayerRepository = get()) }
        factory { ObserveBarcodeDataUseCase() }
        factory {
            SyncWarehouseDataUseCase(
                syncWarehouseRepository = get(),
                authRepository = get()
            )
        }
        factory { LoginUseCase(authRepository = get()) }
        factory { GetPrevLoginUseCase(authRepository = get()) }
        factory { LoadMoveOrdersUseCase(warehouseRepository = get()) }
        factory { SaveSerialToDBUseCase(localWarehouseRepository = get()) }
        factory { SyncToServerSerialsUseCase(syncWarehouseRepository = get()) }
        factory { GetDocumentDataToSyncUseCase(syncWarehouseRepository = get()) }
        factory { GetItemSerialFromDBUseCase(localWarehouseRepository = get()) }
        factory { DeleteSerialNumberUseCase(localWarehouseRepository = get()) }
        factory { SetQuantityGivenUseCase(localWarehouseRepository = get()) }
        factory { SetNoSerialsUseCase(localWarehouseRepository = get()) }
        factory { CheckInputSerialsUseCase(localWarehouseRepository = get()) }
        factory { UpdateSerialNumberUseCase(localWarehouseRepository = get()) }
        factory { DeleteMoveOrderUseCase(localWarehouseRepository = get()) }
        factory { LogoutUseCase(authRepository = get()) }

        factory { GetInventorySegmentUseCase(inventoryRepository = get()) }
        factory { UploadInventoryExelFileUseCase(inventoryRepository = get()) }
        factory { GetInventoryFilesUseCase(inventoryRepository = get()) }
        factory { GetInventoryByFileUseCase(inventoryRepository = get()) }
        factory { DeleteInventoryFileUseCase(inventoryRepository = get()) }
        factory { RenameInventoryFileUseCase(inventoryRepository = get()) }
        factory { UpdateFullInventoryModelUseCase(inventoryRepository = get()) }
        factory { ExportFullInventoryUseCase(inventoryRepository = get()) }
    }

    val parentModule = module {
        includes(
            databaseModule
        )
    }
}