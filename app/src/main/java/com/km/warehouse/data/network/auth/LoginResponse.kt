package com.km.warehouse.data.network.auth

import com.google.gson.annotations.SerializedName

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
data class LoginResponse(
    @SerializedName("accessToken")
    val token: String,
    val type: String,
    val refreshToken: String,
    val id: Long,
    val userName: String,
    val roles: List<String>
)