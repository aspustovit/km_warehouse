package com.km.warehouse.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.km.warehouse.data.network.AuthApiService
import com.km.warehouse.data.network.auth.TokenManager
import com.km.warehouse.data.network.auth.AuthRequest
import com.km.warehouse.data.network.entity.ErrorData
import com.km.warehouse.domain.repository.AuthRepository
import com.km.warehouse.domain.usecase.model.LoginModel
import com.km.warehouse.domain.usecase.model.PrevAuthModel

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */
class AuthRepositoryImpl(val authApiService: AuthApiService, val context: Context) :
    AuthRepository {
    override suspend fun login(auth: AuthRequest): LoginModel {
        try {
            val response = authApiService.login(auth)
            val loginResponse = response.body()
            val tokenManager = TokenManager(context)
            val gson = Gson()

            Log.i("AUTH_RESPONCE", "${gson.toJson(auth)}$loginResponse")
            loginResponse?.let {
                tokenManager.saveToken(it.token)
                tokenManager.saveRefreshToken(it.refreshToken)
                tokenManager.saveLastLogin(it.userName)
            }
            Log.i("AUTH_RESPONCE", "Return")
            var errorData: ErrorData? = null
            if (!response.isSuccessful)
                errorData = parseError(response.errorBody()!!.string())

            return LoginModel(isLoggedIn = response.isSuccessful, error = errorData)
        } catch (ex: Exception) {
            Log.e("AUTH_RESPONCE", "$ex")
            return LoginModel(
                isLoggedIn = false,
                error = ErrorData(
                    status = 600,
                    message = ex.toString(),
                    error = "AuthRepositoryImpl.login()"
                )
            )
        }
    }

    override suspend fun getRefreshLoginToken(): PrevAuthModel {
        val tokenManager = TokenManager(context)
        try {
            Log.v("AuthLogin_S", "${tokenManager.getToken()}")
            val response = authApiService.refreshFullToken(tokenManager.getRefreshToken())
            val tokenResponse = response.body()
            Log.d("AuthLogin_R", "${tokenResponse}")
            if (tokenResponse != null) {
                tokenManager.saveToken(tokenResponse.accessToken)
                tokenManager.saveRefreshToken(tokenResponse.refreshToken)
            } else {
                tokenManager.deleteToken()
                tokenManager.deleteRefreshToken()
            }
        } catch (ex: Exception) {
            Log.e("AuthLogin", "$ex")
            tokenManager.deleteToken()
            tokenManager.deleteRefreshToken()
        }

        Log.d("AuthLogin", "${tokenManager.getLastLogin()}")
        return PrevAuthModel(
            token = tokenManager.getToken(),
            refreshToken = tokenManager.getRefreshToken(),
            userName = tokenManager.getLastLogin()
        )
    }

    private fun parseError(errorBody: String): ErrorData? {
        val gson = Gson()
        val error = gson.fromJson(errorBody, ErrorData::class.java)
        Log.e("syncToServerWarehouseData", "$error")
        return error
    }
}