package com.km.warehouse.ui.auth

import android.util.Log
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.km.warehouse.data.network.auth.AuthRequest
import com.km.warehouse.data.network.entity.ErrorData
import com.km.warehouse.domain.usecase.auth.GetPrevLoginUseCase
import com.km.warehouse.domain.usecase.auth.LoginUseCase
import com.km.warehouse.domain.usecase.model.LoginModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Create by Pustovit Oleksandr on 1/6/2026
 */
class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val getPrevLoginUseCase: GetPrevLoginUseCase
) : ViewModel() {
    private var _authState: MutableStateFlow<AuthState> =
        MutableStateFlow(AuthState(null, LoginModel(isLoggedIn = false, error = null)))
    val viewState: StateFlow<AuthState> = _authState

    fun initAuthState() {
        Log.e("AuthLogin_INIT", "initAuthState")
        viewModelScope.launch {
            getPrevLoginUseCase.invoke(Unit).onSuccess {
                _authState.update { state ->
                    state.copy(prevLogin = it)
                }
            }
        }
    }

    fun logIn(login: String, password: String) {
        _authState.update { state ->
            state.copy(isLoading = true)
        }
        viewModelScope.launch {
            loginUseCase.invoke(AuthRequest(userName = login.toUpperCase(Locale.current), password = password)).onSuccess {
                Log.e("_AUTH_RESPONCE", "$it")
                _authState.update { state ->
                    state.copy(loginModel = it, isLoading = false)
                }
            }.onFailure {
                Log.e("LOG_IN_FAIL", "$it")
                _authState.update { state ->
                    state.copy(
                        loginModel = LoginModel(
                            isLoggedIn = false,
                            error = ErrorData(
                                status = 600,
                                error = "LOG_IN_FAIL",
                                message = it.toString()
                            )
                        ), isLoading = false
                    )
                }
            }
        }
    }

    fun cancelError() {
        _authState.update { state ->
            state.copy(loginModel = LoginModel(isLoggedIn = false, error = null), isLoading = false)
        }
    }
}