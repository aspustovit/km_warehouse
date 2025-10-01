package com.fieldbee.core.ui.compose.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 *
 */
@Immutable
class SuccessColors(
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color
)

/**
 *  Some colors in our app are not part of the color scheme, so we define them here
 *  Be careful and try to not add too many colors here, as it can lead to inconsistency
 *  Create new colors in that one only if they are used not as a part of standard components, but some specific use case
 *  e.g. tram lines colors, field colors, etc.
 */
@Immutable
class MapColors(
    val tramLinesColors: List<Color> = lightTramLinesColors,
    val fieldStrokeColor: Color = Color(0xFFFF832F),
    val routeColor: Color = Color(0xFFFFFFFF)
)

@Immutable
class GLColors(
    val map: Color = Color(0xFFA6A6A6),
    val grid: Color = Color(0x401A1A1A),
    val field: Color = Color(0xFFEBEBEB),
    val boundaries: Color = Color(0xFFF16154),
    val track: Color = Color(0xFF45ACFF),
    val marker: Color = Color(0xFFF3A601),
    val activeGuideLine: Color = Color(0xFFCE46E0),
    val inactiveGuideLine: Color = Color(0xB31A1A1A),
    val uturn: Color = Color(0xFFFF771C),
    val engage: Color = Color(0xFF39973D),
    val disengage: Color = Color(0xFFDB3E30),
    val sectionOff: Color = Color(0xFFCECECE),
    val headland: Color = Color(0x5EFF9800)
)

fun getAdditionalColors(darkTheme: Boolean): AdditionalColors {
    return if (darkTheme) AdditionalColorsDark else AdditionalColorsLight
}

interface AdditionalColors {
    val red300: Color
    val red400: Color
    val red500: Color

    val pink300: Color
    val pink400: Color
    val pink500: Color

    val purple300: Color
    val purple400: Color
    val purple500: Color

    val deepPurple300: Color
    val deepPurple400: Color
    val deepPurple500: Color

    val indigo300: Color
    val indigo400: Color
    val indigo500: Color

    val blue200: Color
    val blue300: Color
    val blue400: Color
    val blue500: Color

    val lightBlue300: Color
    val lightBlue400: Color
    val lightBlue500: Color

    val cyan300: Color
    val cyan400: Color
    val cyan500: Color

    val teal300: Color
    val teal400: Color
    val teal500: Color

    val green300: Color
    val green400: Color
    val green500: Color

    val lightGreen300: Color
    val lightGreen400: Color
    val lightGreen500: Color

    val lime300: Color
    val lime400: Color
    val lime500: Color

    val yellow300: Color
    val yellow400: Color
    val yellow500: Color

    val amber300: Color
    val amber400: Color
    val amber500: Color

    val orange300: Color
    val orange400: Color
    val orange500: Color

    val deepOrange300: Color
    val deepOrange400: Color
    val deepOrange500: Color

    val brown300: Color
    val brown400: Color
    val brown500: Color

    val gray300: Color
    val gray400: Color
    val gray500: Color

    val blueGray300: Color
    val blueGray400: Color
    val blueGray500: Color

    val white: Color
    val black: Color

    val neonRed: Color
    val neonOrange: Color
    val neonYellow: Color
    val neonGreen: Color
    val neonPurple: Color
    val neonCyan: Color
    val neonBlue: Color
}

@Immutable
private object DefaultAdditionalColors : AdditionalColors {
    override val red300: Color = Color(0xFFE57373)
    override val red400: Color = Color(0xFFEF5350)
    override val red500: Color = Color(0xFFF44336)

    override val pink300: Color = Color(0xFFF06292)
    override val pink400: Color = Color(0xFFEC407A)
    override val pink500: Color = Color(0xFFE91E63)

    override val purple300: Color = Color(0xFFBA68C8)
    override val purple400: Color = Color(0xFFAB47BC)
    override val purple500: Color = Color(0xFF9C27B0)

    override val deepPurple300: Color = Color(0xFF9575CD)
    override val deepPurple400: Color = Color(0xFF7E57C2)
    override val deepPurple500: Color = Color(0xFF673AB7)

    override val indigo300: Color = Color(0xFF673AB7)
    override val indigo400: Color = Color(0xFF5C6BC0)
    override val indigo500: Color = Color(0xFF3F51B5)

    override val blue200: Color = Color(0xFF90CAF9)
    override val blue300: Color = Color(0xFF64B5F6)
    override val blue400: Color = Color(0xFF42A5F5)
    override val blue500: Color = Color(0xFF2196F3)

    override val lightBlue300: Color = Color(0xFF4FC3F7)
    override val lightBlue400: Color = Color(0xFF29B6F6)
    override val lightBlue500: Color = Color(0xFF03A9F4)

    override val cyan300: Color = Color(0xFF4DD0E1)
    override val cyan400: Color = Color(0xFF26C6DA)
    override val cyan500: Color = Color(0xFF00BCD4)

    override val teal300: Color = Color(0xFF4DB6AC)
    override val teal400: Color = Color(0xFF26A69A)
    override val teal500: Color = Color(0xFF009688)

    override val green300: Color = Color(0xFF81C784)
    override val green400: Color = Color(0xFF66BB6A)
    override val green500: Color = Color(0xFF4CAF50)

    override val lightGreen300: Color = Color(0xFFAED581)
    override val lightGreen400: Color = Color(0xFF9CCC65)
    override val lightGreen500: Color = Color(0xFF8BC34A)

    override val lime300: Color = Color(0xFFDCE775)
    override val lime400: Color = Color(0xFFD4E157)
    override val lime500: Color = Color(0xFFCDDC39)

    override val yellow300: Color = Color(0xFFFFF176)
    override val yellow400: Color = Color(0xFFFFEE58)
    override val yellow500: Color = Color(0xFFFFEB3B)

    override val amber300: Color = Color(0xFFFFD54F)
    override val amber400: Color = Color(0xFFFFCA28)
    override val amber500: Color = Color(0xFFFFB300)

    override val orange300: Color = Color(0xFFFFB74D)
    override val orange400: Color = Color(0xFFFFA726)
    override val orange500: Color = Color(0xFFFF9800)

    override val deepOrange300: Color = Color(0xFFFF8A65)
    override val deepOrange400: Color = Color(0xFFFF7043)
    override val deepOrange500: Color = Color(0xFFFF5722)

    override val brown300: Color = Color(0xFFA1887F)
    override val brown400: Color = Color(0xFF8D6E63)
    override val brown500: Color = Color(0xFF795548)

    override val gray300: Color = Color(0xFFE0E0E0)
    override val gray400: Color = Color(0xFFBDBDBD)
    override val gray500: Color = Color(0xFF9E9E9E)

    override val blueGray300: Color = Color(0xFF90A4AE)
    override val blueGray400: Color = Color(0xFF78909C)
    override val blueGray500: Color = Color(0xFF607D8B)

    override val white: Color = Color(0xFFFFFFFF)
    override val black: Color = Color(0xFF000000)

    override val neonRed: Color = Color(0xFFFE0000)
    override val neonOrange: Color = Color(0xFFFF7B00)
    override val neonYellow: Color = Color(0xFFFDFE02)
    override val neonGreen: Color = Color(0xFF0BFF01)
    override val neonPurple: Color = Color(0xFFFE00F6)
    override val neonCyan: Color = Color(0xFF00F6FF)
    override val neonBlue: Color = Color(0xFF011EFE)
}

@Immutable
private object AdditionalColorsLight: AdditionalColors by DefaultAdditionalColors

@Immutable
private object AdditionalColorsDark: AdditionalColors by DefaultAdditionalColors

val lightTramLinesColors = listOf(
    Color(0xFFFF832F),
    Color(0xFFFFBC51),
    Color(0xFFFFDB45),
    Color(0xFF78C3FF),
    Color(0xFF8897F6),
)

val recentTrackTramLineColor = Color(0xFF42A5F5)
val tramLineIconDefaultBackgroundColor = Color(0xFFE7E7E7)
val defaultDialogIconColor = Color.Black

val recentTrackSelectionColor: Color
    @Composable get() = MaterialTheme.colorScheme.primary.copy(alpha = 0.12F)

fun getRecentTrackTagColors(): RecentTrackTagColors {
    return RecentTrackTagColorsLight
}

interface RecentTrackTagColors {
    val fertilizing: Color
    val harvesting: Color
    val spraying: Color
    val tilling: Color
    val other: Color
    val planting: Color
    val hayMaking: Color
}

@Immutable
private object RecentTrackTagColorsLight: RecentTrackTagColors {
    override val fertilizing: Color = Color(0xFFFFA726)
    override val harvesting: Color = Color(0xFF81C784)
    override val spraying: Color = Color(0xFF64B5F6)
    override val tilling: Color = Color(0xFFA1887F)
    override val other: Color = Color(0xFFBA68C8)
    override val planting: Color = Color(0xFFFF8A65)
    override val hayMaking: Color = Color(0xFFFFCA28)
}
