package com.fieldbee.core.ui.compose.utils

import android.text.TextUtils
import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 *  <600  - phone
 *  >=600 - tablet
 */
private const val TABLET_MIN_WIDTH = 600

@Stable
@Composable
fun isTablet(): Boolean = LocalConfiguration.current.smallestScreenWidthDp >= TABLET_MIN_WIDTH

@Composable
@Stable
fun Dp.toPx(): Float = with(LocalDensity.current) { this@toPx.toPx() }

@Composable
@ReadOnlyComposable
fun fontDimensionResource(@DimenRes id: Int) = dimensionResource(id = id).value.sp

/**
 * Returns whether the given [CharSequence] contains only digits.
 *
 * @see TextUtils.isDigitsOnly
 */
fun CharSequence.isDigitsOnly(): Boolean = TextUtils.isDigitsOnly(this)

/**
 * Collects values from the provided [Flow] and sends them to the [onEvent] callback as events
 * within a Composable scope.
 *
 * This function is Composable and should be used within the Jetpack Compose UI tree. It sets up
 * a repeating effect that only collects from the [flow] when the current [LifecycleOwner] is in
 * at least the STARTED state, making the flow collection lifecycle-aware.
 *
 * The collection happens on the main thread using [Dispatchers.Main.immediate] to ensure the
 * thread-safety of Composable functions.
 *
 * Example usage within a Composable function:
 * ```kotlin
 * val myFlow = remember { flowOf("Hello", "World") }
 * ObserveAsEvents(myFlow) { value ->
 *     Text("Received event: $value")
 * }
 * ```
 *
 * @param T the type of the data being emitted by the [flow].
 * @param flow the [Flow] to be collected.
 * @param onEvent a lambda function to be invoked with each value emitted by the [flow].
 */
@Composable
fun <T> ObserveAsEvents(flow: Flow<T>, onEvent: (T) -> Unit) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    LaunchedEffect(key1 = flow, key2 = lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}
