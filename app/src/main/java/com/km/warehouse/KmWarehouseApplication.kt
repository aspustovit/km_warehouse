package com.km.warehouse

import android.app.Application
import android.util.Log
import com.km.warehouse.di.startApplicationKoin

/**
 * Create by Pustovit Oleksandr on 9/24/2025
 */
class KmWarehouseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startApplicationKoin(this)
        Log.d("KmWarehouseApplication", "APP START")
    }
}