package com.km.warehouse.di

import android.content.Context
import com.km.warehouse.data.AppDataModule
import com.km.warehouse.data.KmWarehouseDatabase
import com.km.warehouse.domain.DomainKoinModule
import com.km.warehouse.ui.AppViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

/**
 * Create by Pustovit Oleksandr on 9/23/2025
 */

fun startApplicationKoin(applicationContext: Context) {
    startKoin {
        androidContext(applicationContext)
        modules(
            statisticsModule,
            AppDataModule.parentModule,
            DomainKoinModule.parentModule,
            AppViewModelModule.parentModule
        )
    }
}

private val statisticsModule = module {
    single { KmWarehouseDatabase.getInstance(context = get()) }
}