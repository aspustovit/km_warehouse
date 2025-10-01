package com.fieldbee.core.ui.compose.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontLoadingStrategy
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.LineHeightStyle.Alignment
import androidx.compose.ui.text.style.LineHeightStyle.Trim
import androidx.compose.ui.unit.sp
import com.km.warehouse.R

internal fun getTypography(isTablet: Boolean): Typography =
    if (isTablet) TabletTypography else MobileTypography

internal fun getAdditionalTypography(isTablet: Boolean): AdditionalTypography {
    return if (isTablet) TabletAdditionalTypography else MobileAdditionalTypography
}

@OptIn(ExperimentalTextApi::class)
private val robotoFontFamily = FontFamily(
    variationFont(R.font.roboto),
    variationFont(R.font.roboto, weight = FontWeight.W400),
    variationFont(R.font.roboto, weight = FontWeight.W500),
)

@ExperimentalTextApi
private fun variationFont(
    resId: Int,
    weight: FontWeight = FontWeight.Normal,
    style: FontStyle = FontStyle.Normal,
    loadingStrategy: FontLoadingStrategy = FontLoadingStrategy.Blocking,
    variationSettings: FontVariation.Settings = FontVariation.Settings(weight, style),
): Font = Font(
    resId,
    weight,
    style,
    loadingStrategy,
    variationSettings
)

/**
 * Typography for mobile devices
 */
private val MobileTypography: Typography = withFontFamily(robotoFontFamily) {
    Typography(
        displayLarge = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.25).sp,
        ),
        displayMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 45.sp,
            lineHeight = 52.sp,
            letterSpacing = 0.sp,
        ),
        displaySmall = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp,
        ),
        headlineLarge = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp,
        ),
        headlineMedium = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp,
        ),
        headlineSmall = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp,
            lineHeightStyle = LineHeightStyle(
                alignment = Alignment.Bottom,
                trim = Trim.None,
            ),
        ),
        titleLarge = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
            lineHeightStyle = LineHeightStyle(
                alignment = Alignment.Bottom,
                trim = Trim.LastLineBottom,
            ),
        ),
        titleMedium = TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp,
        ),
        titleSmall = TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
        ),
        // Used for Button
        labelLarge = TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
            lineHeightStyle = LineHeightStyle(
                alignment = Alignment.Center,
                trim = Trim.LastLineBottom,
            ),
        ),
        // Used for Navigation items
        labelMedium = TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
            lineHeightStyle = LineHeightStyle(
                alignment = Alignment.Center,
                trim = Trim.LastLineBottom,
            ),
        ),
        // Used for Tag
        labelSmall = TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
            lineHeightStyle = LineHeightStyle(
                alignment = Alignment.Center,
                trim = Trim.LastLineBottom,
            ),
        ),
        // Default text style
        bodyLarge = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
            lineHeightStyle = LineHeightStyle(
                alignment = Alignment.Center,
                trim = Trim.None,
            ),
        ),
        bodyMedium = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp,
        ),
        bodySmall = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp,
        ),
    )
}

interface AdditionalTypography {
    val labelOverline: TextStyle
}

@Immutable
private object DefaultAdditionalTypography : AdditionalTypography {
    override val labelOverline: TextStyle = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = Alignment.Center,
            trim = Trim.LastLineBottom,
        ),
    )
}

@Immutable
private object MobileAdditionalTypography : AdditionalTypography by DefaultAdditionalTypography

@Immutable
private object TabletAdditionalTypography : AdditionalTypography {
    override val labelOverline: TextStyle = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = Alignment.Center,
            trim = Trim.LastLineBottom,
        ),
    )
}

/**
 * Typography for tablet
 */
private val TabletTypography: Typography = withFontFamily(robotoFontFamily) {
    Typography(
        displayLarge = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 72.sp,
            lineHeight = 80.sp,
            letterSpacing = (-0.25).sp,
        ),
        displayMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 52.sp,
            lineHeight = 56.sp,
            letterSpacing = 0.sp,
        ),
        displaySmall = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 44.sp,
            lineHeight = 52.sp,
            letterSpacing = 0.sp,
        ),
        headlineLarge = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 40.sp,
            lineHeight = 48.sp,
            letterSpacing = 0.sp,
        ),
        headlineMedium = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp,
        ),
        headlineSmall = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp,
            lineHeightStyle = LineHeightStyle(
                alignment = Alignment.Bottom,
                trim = Trim.None,
            ),
        ),
        titleLarge = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 28.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp,
            lineHeightStyle = LineHeightStyle(
                alignment = Alignment.Bottom,
                trim = Trim.LastLineBottom,
            ),
        ),
        titleMedium = TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 24.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.15.sp,
        ),
        titleSmall = TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 22.sp,
            lineHeight = 26.sp,
            letterSpacing = 0.1.sp,
        ),
        // Used for Button
        labelLarge = TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 22.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.1.sp,
            lineHeightStyle = LineHeightStyle(
                alignment = Alignment.Center,
                trim = Trim.LastLineBottom,
            ),
        ),
        // Used for Navigation items
        labelMedium = TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 18.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.5.sp,
            lineHeightStyle = LineHeightStyle(
                alignment = Alignment.Center,
                trim = Trim.LastLineBottom,
            ),
        ),
        // Used for Tag
        labelSmall = TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.5.sp,
            lineHeightStyle = LineHeightStyle(
                alignment = Alignment.Center,
                trim = Trim.LastLineBottom,
            ),
        ),
        // Default text style
        bodyLarge = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.5.sp,
            lineHeightStyle = LineHeightStyle(
                alignment = Alignment.Center,
                trim = Trim.None,
            ),
        ),
        bodyMedium = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 20.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.25.sp,
        ),
        bodySmall = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.25.sp,
        ),
    )
}

private fun withFontFamily(fontFamily: FontFamily, block: () -> Typography): Typography {
    return with(block()) {
        copy(
            displayLarge = displayLarge.copy(fontFamily = fontFamily),
            displayMedium = displayMedium.copy(fontFamily = fontFamily),
            displaySmall = displaySmall.copy(fontFamily = fontFamily),

            headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
            headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
            headlineSmall = headlineSmall.copy(fontFamily = fontFamily),

            titleLarge = titleLarge.copy(fontFamily = fontFamily),
            titleMedium = titleMedium.copy(fontFamily = fontFamily),
            titleSmall = titleSmall.copy(fontFamily = fontFamily),

            bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
            bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
            bodySmall = bodySmall.copy(fontFamily = fontFamily),

            labelLarge = labelLarge.copy(fontFamily = fontFamily),
            labelMedium = labelMedium.copy(fontFamily = fontFamily),
            labelSmall = labelSmall.copy(fontFamily = fontFamily),
        )
    }
}
