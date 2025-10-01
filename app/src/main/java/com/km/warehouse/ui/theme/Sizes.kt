package com.fieldbee.core.ui.compose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fieldbee.core.ui.compose.theme.PaddingsDefaults.PaddingExtraLarge
import com.fieldbee.core.ui.compose.theme.PaddingsDefaults.PaddingExtraSmall
import com.fieldbee.core.ui.compose.theme.PaddingsDefaults.PaddingLarge
import com.fieldbee.core.ui.compose.theme.PaddingsDefaults.PaddingMedium
import com.fieldbee.core.ui.compose.theme.PaddingsDefaults.PaddingSmall

object PaddingsDefaults {

    val PaddingExtraSmall: Dp = 4.dp

    val PaddingSmall: Dp = 5.dp

    val PaddingMedium: Dp = 10.dp

    val PaddingLarge: Dp = 15.dp

    val PaddingExtraLarge: Dp = 20.dp
}

object StrokeDefaults {
    val StrokeWidthExtraThin: Dp = 1.dp
    val StrokeWidthThin: Dp = 2.dp
}

@Immutable
open class Sizes {
    open val paddingExtraSmall: Dp = PaddingExtraSmall
    open val paddingSmall: Dp = PaddingSmall
    open val paddingMedium: Dp = PaddingMedium
    open val paddingLarge: Dp = PaddingLarge
    open val paddingExtraLarge: Dp = PaddingExtraLarge
    open val defaultStrokeWidth: Dp = StrokeDefaults.StrokeWidthExtraThin
    open val boldStrokeWidth: Dp = StrokeDefaults.StrokeWidthThin
    open val mapButtonSize: Dp = 96.dp
    open val mapBigFabSize: Dp = 80.dp
    open val mapControlsMediumFabSize: Dp = 52.dp
    open val mapControlsFabIconSize: Dp = 40.dp
    open val deviationBarHeight: Dp = 100.dp
    open val bottomNavigationWidgetHeight: Dp = 84.dp
    open val shiftWindowWidth: Dp = 270.dp
}

@Immutable
class TabletSizes : Sizes()

fun getThemeSizes(isTablet: Boolean): Sizes {
    return if (isTablet) TabletSizes() else PortraitSizes()
}

@Immutable
class PortraitSizes : Sizes() {
    override val paddingExtraSmall: Dp = PaddingExtraSmall
    override val paddingSmall: Dp = PaddingSmall
    override val paddingMedium: Dp = PaddingMedium
    override val paddingLarge: Dp = PaddingLarge
    override val paddingExtraLarge: Dp = PaddingExtraLarge
    override val mapControlsMediumFabSize = 34.dp
    override val mapBigFabSize: Dp = 60.dp
    override val mapControlsFabIconSize: Dp = 24.dp
    override val deviationBarHeight: Dp = 68.dp
    override val bottomNavigationWidgetHeight: Dp = 64.dp
    override val shiftWindowWidth: Dp = 180.dp
}

// Get Material3 Theme
internal fun getMaterial3ThemeSizes(isTablet: Boolean): SizesMaterial3 {
    return if (isTablet) TabletSizesMaterial3 else MobileSizesMaterial3
}

interface SizesMaterial3 {
    val padding3XS: Dp
    val padding2XS: Dp
    val paddingExtraSmall: Dp
    val paddingSmall: Dp
    val paddingMedium: Dp
    val paddingExtraMedium: Dp
    val paddingLarge: Dp
    val paddingExtraLarge: Dp
    val padding2XL: Dp
    val padding3XL: Dp
    val padding4XL: Dp

    val defaultStrokeWidth: Dp
    val boldStrokeWidth: Dp
    val mapControlsMediumFabSize: Dp
    val mapButtonSize: Dp
    val mapControlsFabIconSize: Dp
    val mapBigFabSize: Dp
    val deviationBarHeight: Dp
    val bottomNavigationWidgetHeight: Dp
    val shiftWindowWidth: Dp

    val iconDefaultSize: Dp
    val iconRecentTrackSize: Dp

    val speedometerArcStrokeWidth: Dp
}

@Immutable
private object DefaultSizesMaterial3 : SizesMaterial3 {
    override val padding3XS: Dp = 0.dp
    override val padding2XS: Dp = 2.dp
    override val paddingExtraSmall: Dp = 4.dp
    override val paddingSmall: Dp = 8.dp
    override val paddingMedium: Dp = 12.dp
    override val paddingExtraMedium: Dp = 14.dp
    override val paddingLarge: Dp = 16.dp
    override val paddingExtraLarge: Dp = 20.dp
    override val padding2XL: Dp = 24.dp
    override val padding3XL: Dp = 32.dp
    override val padding4XL: Dp = 40.dp

    override val defaultStrokeWidth: Dp = StrokeDefaults.StrokeWidthExtraThin
    override val boldStrokeWidth: Dp = StrokeDefaults.StrokeWidthThin

    override val mapControlsMediumFabSize = 40.dp
    override val mapButtonSize: Dp = 40.dp
    override val mapControlsFabIconSize: Dp = 56.dp
    override val mapBigFabSize: Dp = 56.dp

    override val deviationBarHeight: Dp = 68.dp
    override val bottomNavigationWidgetHeight: Dp = 64.dp
    override val shiftWindowWidth: Dp = 180.dp

    override val iconDefaultSize: Dp = 24.dp
    override val iconRecentTrackSize: Dp = 56.dp

    override val speedometerArcStrokeWidth: Dp = 8.dp
}

@Immutable
private object MobileSizesMaterial3 : SizesMaterial3 by DefaultSizesMaterial3

@Immutable
private object TabletSizesMaterial3 : SizesMaterial3 {
    override val padding3XS: Dp = 2.dp
    override val padding2XS: Dp = 4.dp
    override val paddingExtraSmall: Dp = 8.dp
    override val paddingSmall: Dp = 12.dp
    override val paddingMedium: Dp = 16.dp
    override val paddingExtraMedium: Dp = 20.dp
    override val paddingLarge: Dp = 24.dp
    override val paddingExtraLarge: Dp = 32.dp
    override val padding2XL: Dp = 40.dp
    override val padding3XL: Dp = 48.dp
    override val padding4XL: Dp = 56.dp

    override val mapControlsMediumFabSize: Dp = 56.dp
    override val mapControlsFabIconSize: Dp = 56.dp
    override val mapButtonSize: Dp = 96.dp
    override val mapBigFabSize: Dp = 80.dp

    override val defaultStrokeWidth: Dp = StrokeDefaults.StrokeWidthExtraThin
    override val boldStrokeWidth: Dp = StrokeDefaults.StrokeWidthThin
    override val deviationBarHeight: Dp = 100.dp
    override val bottomNavigationWidgetHeight: Dp = 84.dp
    override val shiftWindowWidth: Dp = 270.dp

    override val iconDefaultSize: Dp = DefaultSizesMaterial3.iconDefaultSize
    override val iconRecentTrackSize: Dp = 80.dp

    override val speedometerArcStrokeWidth: Dp = DefaultSizesMaterial3.defaultStrokeWidth
}
