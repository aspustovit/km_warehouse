package com.km.warehouse.ui

import com.km.warehouse.ui.auth.AuthViewModel
import com.km.warehouse.ui.move_order.MoveOrderItemViewModel
import com.km.warehouse.ui.sync.SyncViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Create by Pustovit Oleksandr on 9/24/2025
 */
object AppViewModelModule {
    private val viewModulesModule = module {
        viewModel {
            SharedViewModel(
                observeBarcodeDataUseCase = get()
            )
        }
        viewModel {
            MoveOrderItemViewModel(
                loadMoveOrdersUseCase = get(),
                observeBarcodeDataUseCase = get(),
                saveSerialToDBUseCase = get(),
                syncToServerSerialsUseCase = get(),
                getPrevLoginUseCase = get()
            )
        }
        viewModel {
            SyncViewModel(syncWarehouseDataUseCase = get())
        }
        viewModel { AuthViewModel(loginUseCase = get(), getPrevLoginUseCase = get()) }
    }

    val parentModule =
        module {
            includes(
                viewModulesModule
            )
        }
}