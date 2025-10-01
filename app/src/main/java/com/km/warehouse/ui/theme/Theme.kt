package com.fieldbee.core.ui.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.fieldbee.core.ui.compose.utils.isTablet

val LocalMapColors = compositionLocalOf {
    MapColors()
}
val LocalGLColors = compositionLocalOf {
    GLColors()
}

val LocalSizes: ProvidableCompositionLocal<Sizes> = compositionLocalOf { Sizes() }

val LocalSizesMaterial3: ProvidableCompositionLocal<SizesMaterial3> = compositionLocalOf {
    error("LocalSizesMaterial3 not provided")
}

val LocalAdditionalColors: ProvidableCompositionLocal<AdditionalColors> = compositionLocalOf {
    error("LocalAdditionalColors not provided")
}

val LocalAdditionalTypography: ProvidableCompositionLocal<AdditionalTypography> =
    compositionLocalOf {
        error("LocalAdditionalTypography not provided")
    }

val LocalSuccessColors = compositionLocalOf {
    SuccessColors(
        success = Color.Unspecified,
        onSuccess = Color.Unspecified,
        successContainer = Color.Unspecified,
        onSuccessContainer = Color.Unspecified
    )
}

@Composable
fun FieldbeeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) WarehouseDarkColorScheme else WarehouseLightColorScheme
    val successColorScheme =
        if (darkTheme) WarehouseDarkSuccessColorScheme else WarehouseLightSuccessColorScheme

    BaseTheme(
        darkTheme = darkTheme,
        colorScheme = colorScheme,
        successColorScheme = successColorScheme,
        content = content
    )
}

@Composable
fun TafeTheme(
    darkTheme: Boolean = false, // isSystemInDarkTheme()
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) TafeDarkColorScheme else TafeLightColorScheme
    val successColorScheme =
        if (darkTheme) TafeDarkSuccessColorScheme else TafeLightSuccessColorScheme

    BaseTheme(
        darkTheme = darkTheme,
        colorScheme = colorScheme,
        successColorScheme = successColorScheme,
        content = content
    )
}

@Composable
fun BaseTheme(
    darkTheme: Boolean = false,
    colorScheme: ColorScheme,
    successColorScheme: SuccessColors,
    content: @Composable () -> Unit
) {
    val mapColors = MapColors()
    val glColors = GLColors()
    val additionalColorScheme: AdditionalColors = getAdditionalColors(darkTheme)
    val typography: Typography = getTypography(isTablet = isTablet())
    val sizes: Sizes = getThemeSizes(isTablet())
    val additionalTypography: AdditionalTypography = getAdditionalTypography(isTablet())
    val sizesMaterial3: SizesMaterial3 = getMaterial3ThemeSizes(isTablet())

    CompositionLocalProvider(
        LocalSuccessColors provides successColorScheme,
        LocalMapColors provides mapColors,
        LocalGLColors provides glColors,
        LocalSizes provides sizes,
        LocalAdditionalColors provides additionalColorScheme,
        LocalAdditionalTypography provides additionalTypography,
        LocalSizesMaterial3 provides sizesMaterial3,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}

object ThemeHelper {
    val colorScheme: ColorScheme
        @Composable
        get() = MaterialTheme.colorScheme

    val sizes: Sizes
        @Composable
        get() = LocalSizes.current

    val sizesMaterial3: SizesMaterial3
        @Composable
        get() = LocalSizesMaterial3.current

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes

    val mapColors: MapColors
        @Composable
        get() = LocalMapColors.current

    val successColors: SuccessColors
        @Composable
        get() = LocalSuccessColors.current
}
