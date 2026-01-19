package com.km.warehouse.data.network.auth

import com.google.gson.annotations.SerializedName

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
data class TokenEntity(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("tokenType")
    val tokenType: String
)