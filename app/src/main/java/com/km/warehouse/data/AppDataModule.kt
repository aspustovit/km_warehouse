package com.km.warehouse.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.km.warehouse.data.network.AuthApiService
import com.km.warehouse.data.network.AuthInterceptor
import com.km.warehouse.data.network.WarehouseApiService
import com.km.warehouse.data.repository.AuthRepositoryImpl
import com.km.warehouse.data.repository.BayerRepositoryImpl
import com.km.warehouse.data.repository.SyncWarehouseRepositoryImpl
import com.km.warehouse.domain.repository.AuthRepository
import com.km.warehouse.domain.repository.LocalWarehouseRepository
import com.km.warehouse.domain.repository.SyncWarehouseRepository
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Create by Pustovit Oleksandr on 9/23/2025
 */
object AppDataModule {
    private val retrofitClient = named("warehouseRetrofitClient")
    private val retrofitClientTimeout = 10L
    private val baseUrl = "http://172.18.0.60:8081/api/"
    //private val baseUrl = "http://192.168.0.167:8081/api/" // Test server

    private val networkModule = module {
        koinApplication()
        single(qualifier = retrofitClient) {
            OkHttpClient.Builder()
                .addInterceptor(interceptor = AuthInterceptor(context = get()))
                .readTimeout(timeout = retrofitClientTimeout, unit = TimeUnit.SECONDS)
                .connectTimeout(timeout = retrofitClientTimeout, unit = TimeUnit.SECONDS)
                .writeTimeout(timeout = retrofitClientTimeout, unit = TimeUnit.SECONDS)
                .build()
        }

        single<Gson>(qualifier = retrofitClient) { GsonBuilder().create() }

        single<Retrofit>(qualifier = retrofitClient) {
            Retrofit.Builder().baseUrl(baseUrl)
                .client(get(qualifier = retrofitClient))
                .addConverterFactory(GsonConverterFactory.create(get(qualifier = retrofitClient)))
                .build()
        }
        single<AuthApiService> {
            get<Retrofit>(qualifier = retrofitClient).create(
                AuthApiService::class.java
            )
        }
        single<WarehouseApiService> {
            get<Retrofit>(qualifier = retrofitClient).create(
                WarehouseApiService::class.java
            )
        }
    }

    private val repositoryModule = module {
        single<AuthRepository> { AuthRepositoryImpl(authApiService = get(), context = get()) }

        single<SyncWarehouseRepository> {
            SyncWarehouseRepositoryImpl(
                warehouseApiService = get(),
                database = get()
            )
        }
        single<LocalWarehouseRepository> {
            BayerRepositoryImpl(
                database = get()
            )
        }
    }

    val parentModule =
        module {
            includes(networkModule, repositoryModule)
        }
}