package com.km.warehouse.ui

import com.km.warehouse.ui.move_order.MoveOrderItemViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Create by Pustovit Oleksandr on 9/24/2025
 */
object AppViewModelModule {
    private val viewModulesModule = module {
        viewModel { SharedViewModel(observeBarcodeDataUseCase = get()) }
        viewModel {
            MoveOrderItemViewModel(loadBayerUseCase = get())
        }
    }

    val parentModule =
        module {
            includes(
                viewModulesModule
            )
        }
}