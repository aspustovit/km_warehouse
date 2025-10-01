package com.km.warehouse.domain

import com.km.warehouse.domain.usecase.LoadBayerUseCase
import com.km.warehouse.domain.usecase.ObserveBarcodeDataUseCase
import org.koin.dsl.module

/**
 * Create by Pustovit Oleksandr on 9/24/2025
 */
object DomainKoinModule {
    private val databaseModule = module {
        factory { LoadBayerUseCase(bayerRepository = get()) }
        factory { ObserveBarcodeDataUseCase() }
    }

    val parentModule = module {
        includes(
            databaseModule
        )
    }
}