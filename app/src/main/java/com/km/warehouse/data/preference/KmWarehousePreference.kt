package com.km.warehouse.data.preference

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Create by Pustovit Oleksandr on 1/6/2026
 */
object KmWarehousePreference {
    val TOKENS_PREF: String = "tokens"
    val REFRESH_TOKEN_KEY: String = "jwt_refresh_token"
    val TOKEN_KEY: String = "jwt_token"
    val LAST_LOGIN: String = "last_login"

    lateinit var tokenPrefs: SharedPreferences

    fun init(context: Context) {
        tokenPrefs = context.getSharedPreferences(TOKENS_PREF, Context.MODE_PRIVATE)
    }


}