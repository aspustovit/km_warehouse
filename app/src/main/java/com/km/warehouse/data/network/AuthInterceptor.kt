package com.km.warehouse.data.network

import android.content.Context
import android.util.Log
import com.km.warehouse.data.network.auth.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
class AuthInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
        val token = TokenManager(context).getToken()
        Log.v("TOKEN_REFRESH_INTR", token)
        if(token.isNotEmpty()){
            newRequest.header("Authorization", "Bearer $token")
        }
        return chain.proceed(newRequest.build())
    }
}