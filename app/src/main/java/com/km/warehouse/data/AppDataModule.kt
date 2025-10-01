package com.km.warehouse.data

import com.km.warehouse.data.repository.BayerRepositoryImpl
import com.km.warehouse.domain.repository.BayerRepository
import org.koin.dsl.module

/**
 * Create by Pustovit Oleksandr on 9/23/2025
 */
object AppDataModule {
    private val repositoryModule = module {
        single<BayerRepository> {
            BayerRepositoryImpl(
                database = get()
            )
        }
    }

    val parentModule =
        module {
            includes(repositoryModule)
        }
}