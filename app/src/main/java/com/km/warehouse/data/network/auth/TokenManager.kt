package com.km.warehouse.data.network.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.auth0.jwt.JWT
import com.km.warehouse.data.preference.KmWarehousePreference
import com.km.warehouse.data.preference.KmWarehousePreference.LAST_LOGIN
import com.km.warehouse.data.preference.KmWarehousePreference.REFRESH_TOKEN_KEY
import com.km.warehouse.data.preference.KmWarehousePreference.TOKEN_KEY
import java.util.Date

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
class TokenManager(private val context: Context) {
    val tokenPreferences: SharedPreferences =
        context.getSharedPreferences(KmWarehousePreference.TOKENS_PREF, Context.MODE_PRIVATE)

    fun getToken(): String {
        val tokenString = tokenPreferences.getString(TOKEN_KEY, "")!!
        Log.i("TOKEN_REFRESH_S", tokenString)
        if (tokenString.isEmpty())
            return ""

        val jwt = JWT.decode(tokenString)
        if (jwt.expiresAt.before(Date())) {
            val refreshTokenString = getRefreshToken()
            Log.e("TOKEN_REFRESH", jwt.expiresAt.before(Date()).toString())
            val refreshJwt = JWT.decode(refreshTokenString)
            Log.i("TOKEN_REFRESH_", refreshJwt.expiresAt.before(Date()).toString())
            if (refreshJwt.expiresAt.before(Date())) {
                return ""
            }
        }

        return tokenString
    }

    fun saveToken(token: String) {
        tokenPreferences.edit()
            .putString(TOKEN_KEY, token)
            .apply()
    }

    fun deleteToken() {
        tokenPreferences.edit()
            .putString(TOKEN_KEY, "")
            .apply()
    }

    fun getRefreshToken(): String {
        return tokenPreferences.getString(REFRESH_TOKEN_KEY, "")!!
    }

    fun saveRefreshToken(token: String) {
        tokenPreferences.edit()
            .putString(REFRESH_TOKEN_KEY, token)
            .apply()
    }

    fun saveLastLogin(login: String) {
        tokenPreferences.edit()
            .putString(LAST_LOGIN, login)
            .apply()
    }

    fun getLastLogin(): String =
        tokenPreferences.getString(LAST_LOGIN, "")!!


    fun deleteRefreshToken() {
        tokenPreferences.edit()
            .putString(REFRESH_TOKEN_KEY, "")
            .apply()
    }

    fun saveUserRoles(token: String) {
    }
}