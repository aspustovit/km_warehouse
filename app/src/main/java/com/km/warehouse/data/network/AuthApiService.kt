package com.km.warehouse.data.network

import com.km.warehouse.data.network.auth.AuthRequest
import com.km.warehouse.data.network.auth.LoginResponse
import com.km.warehouse.data.network.auth.TokenEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
interface AuthApiService {
    @POST("auth/login")
    suspend fun login(
        @Body auth: AuthRequest,
    ): Response<LoginResponse>

    @GET("auth/refresh")
    suspend fun refreshToken(
        @Header("Authorization") token: String,
    ): Response<String>

    @GET("auth/refresh_full")
    suspend fun refreshFullToken(
        @Header("Authorization") token: String,
    ): Response<TokenEntity>
}