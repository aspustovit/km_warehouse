package com.km.warehouse.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.km.warehouse.MainActivity
import com.km.warehouse.R
import com.km.warehouse.data.network.auth.TokenManager
import com.km.warehouse.ui.sync.ErrorDialog
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel

/**
 * Create by Pustovit Oleksandr on 1/6/2026
 */
class AuthActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tokenManager = TokenManager(this@AuthActivity)

        setContent {
            val viewModel: AuthViewModel = koinViewModel()
            val state = viewModel.viewState.collectAsState()
            val prevLogin = state.value.prevLogin
            LaunchedEffect(prevLogin != null && prevLogin.token.isNotBlank(), state.value.loginModel.isLoggedIn) {
                state.value.apply {
                    Log.d("AUTH_RESPONCE_", "${state.value.prevLogin?.userName}")
                    if (prevLogin != null && prevLogin.token.isNotBlank()) {
                        val intent = Intent(this@AuthActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        if (loginModel.isLoggedIn) {
                            val intent = Intent(this@AuthActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
            LaunchedEffect(Unit) {
                viewModel.initAuthState()
            }
            MaterialTheme { // Apply your app's theme
                Log.d(
                    "_AUTH_RESPONCE_",
                    "${state.value.loginModel.isLoggedIn} - ${state.value.loginModel.errorText}"
                )
                if (!state.value.loginModel.isLoggedIn && state.value.loginModel.errorText.isNotBlank()) {
                    ErrorDialog(errorMessage = state.value.loginModel.errorText, onDismiss = {
                        viewModel.cancelError()
                    })
                }
                LoginScreen(
                    userLogin = tokenManager.getLastLogin(),
                    isLoading = state.value.isLoading,
                    onStartLogin = { login, pass ->
                        viewModel.logIn(login, pass)
                    })
            }
        }
    }

    @Composable
    fun LoginScreen(
        userLogin: String,
        isLoading: Boolean,
        onStartLogin: (String, String) -> Unit
    ) {
        val focusRequester = remember { FocusRequester() }
        var login by remember { mutableStateOf(userLogin) }
        var password by remember { mutableStateOf("") }
        var showPasswordIsEmptyError by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.auth_message), fontSize = 28.sp) //

            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text(stringResource(R.string.login)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .focusRequester(focusRequester)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .focusRequester(focusRequester),
                visualTransformation = PasswordVisualTransformation(),
                isError = showPasswordIsEmptyError,
                supportingText = { if(showPasswordIsEmptyError) Text(stringResource(R.string.password_is_empty)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if(password.isEmpty()){
                        showPasswordIsEmptyError = true
                        return@Button
                    }
                    showPasswordIsEmptyError = false
                    onStartLogin(
                        if (login == null || login.isEmpty()) userLogin else login,
                        password
                    )
                    focusRequester.freeFocus()
                },
                modifier = Modifier
                    .fillMaxWidth().height(56.dp),
                enabled = !isLoading,
            ) {
                Text(text = stringResource(R.string.log_in), fontSize = 20.sp)
            }

            if (!isLoading)
                return

            Spacer(modifier = Modifier.height(12.dp))

            AuthLoader(
                modifier = Modifier
                    .height(136.dp)
                    .width(260.dp)
            )
        }
    }

    @Composable
    fun AuthLoader(modifier: Modifier = Modifier) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.storage))
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            modifier = modifier,
            composition = composition,
            progress = { progress }
        )
    }
}