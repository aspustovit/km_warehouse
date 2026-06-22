package com.km.warehouse.data.network

import android.content.Context
import android.util.Log
import com.km.warehouse.data.network.auth.AuthRequest
import com.km.warehouse.data.network.auth.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/**
 * Create by Pustovit Oleksandr on 15/06/2026
 */
class TokenAuthenticator(val context: Context, val authApiService: AuthApiService) : Authenticator {
    private var retryCount = 0

    override fun authenticate(route: Route?, prevResponse: Response): Request? {
        synchronized(this) { // Prevent multiple refresh calls
            if (retryCount >= 3) {
                // Reset retry count once reached max attempts
                retryCount = 0
                null
            } else {
                retryCount++
                try {
                    val tokenManager = TokenManager(context)
                    val authRequest = AuthRequest(
                        userName = tokenManager.getLastLogin(),
                        password = tokenManager.getPass()
                    )
                    // Fetch new tokens synchronously
                    Log.d("AuthLogin_RE", "${authRequest}")
                    val response = authApiService.login(authRequest).execute()
                    val tokenResponse = response.body()
                    Log.d("AuthLogin_R", "${tokenResponse}")
                    if (tokenResponse != null) {
                        tokenManager.saveToken(tokenResponse.token)
                        tokenManager.saveRefreshToken(tokenResponse.refreshToken)
                        // Retry the original request with new token
                        return prevResponse.request.newBuilder()
                            .header("Authorization", "Bearer ${tokenResponse.token}")
                            .build()
                    } else {
                        Log.i("AuthLogin_R", "tokenResponse = ${tokenResponse}")
                        tokenManager.deleteToken()
                        tokenManager.deleteRefreshToken()
                        return null
                    }
                } catch (ex: Exception) {
                    Log.e("AuthLogin_R", "${ex}")
                    return null
                }

            }
        }
        return null
    }
}