package com.fieldbee.core.ui.compose.theme

import androidx.compose.ui.graphics.Color

internal object ColorsWarehouse {
    /**
     * Light FieldBee theme colors
     */
    internal object Light {

        // Primary
        internal val primary = Color(0xFFFFB300)
        internal val onPrimary = Color(0xFF191200)
        internal val primaryContainer = Color(0xFFFFF0CC)
        internal val onPrimaryContainer = Color(0xFFFFB300)
        internal val inversePrimary = Color(0xFFFFC233)

        // Secondary
        internal val secondary = Color(0xFF333333)
        internal val onSecondary = Color(0xFFE3E3E3)
        internal val secondaryContainer = Color(0xFFE5E5E5)
        internal val onSecondaryContainer = Color(0xFF333333)

        // Tertiary
        internal val tertiary = Color(0xFF1976D2)
        internal val onTertiary = Color(0xFFE3E3E3)
        internal val tertiaryContainer = Color(0xFFCDE3F9)
        internal val onTertiaryContainer = Color(0xFF1976D2)

        // Surface
        internal val surface = Color(0xFFF7F7F7)
        internal val onSurface = Color(0xFF121212)
        internal val surfaceVariant = Color(0xFFDEDEDE)
        internal val onSurfaceVariant = Color(0xFF696969)
        internal val inverseSurface = Color(0xFF333333)
        internal val inverseOnSurface = Color(0xFFF7F7F7)

        // Background
        internal val background = Color(0xFFF7F7F7)
        internal val onBackground = Color(0xFF121212)

        // Error
        internal val error = Color(0xFFD32F2F)
        internal val onError = Color(0xFFE3E3E3)
        internal val errorContainer = Color(0xFFF9E4E4)
        internal val onErrorContainer = Color(0xFFD32F2F)

        // Success
        internal val success = Color(0xFF4CAF50)
        internal val onSuccess = Color(0xFFFFFFFF)
        internal val successContainer = Color(0xFFDBF0DC)
        internal val onSuccessContainer = Color(0xFF3C8B3F)

        // Outline
        internal val outline = Color(0xFFADADAD)
        internal val outlineVariant = Color(0xFFE0E0E0)

        // Scrim
        internal val scrim = Color(0xFF000000)

        // Surface Container
        internal val surfaceDim = Color(0xFFCFCFCF)
        internal val surfaceBright = Color(0xFFF7F7F7)
        internal val surfaceContainer = Color(0xFFF0F0F0)
        internal val surfaceContainerHigh = Color(0xFFE8E8E8)
        internal val surfaceContainerHighest = Color(0xFFDEDEDE)
        internal val surfaceContainerLow = Color(0xFFF4F4F4)
        internal val surfaceContainerLowest = Color(0xFFFFFFFF)
    }

    /**
     * Dark FieldBee theme colors
     */
    internal object Dark {

        // Primary
        internal val primary = Color(0xFFFFCE1F)
        internal val onPrimary = Color(0xFF111318)
        internal val primaryContainer = Color(0xFF2C2E33)
        internal val onPrimaryContainer = Color(0xFF8C8C8C)
        internal val inversePrimary = Color(0xFF171615)

        // Secondary
        internal val secondary = Color(0xFFFFFFFF)
        internal val onSecondary = Color(0xFF0C0E13)
        internal val secondaryContainer = Color(0xFF2D3036)
        internal val onSecondaryContainer = Color(0xFFFFCE1F)

        // Tertiary
        internal val tertiary = Color(0xFF97CCF8)
        internal val onTertiary = Color(0xFF00344F)
        internal val tertiaryContainer = Color(0xFF014B71)
        internal val onTertiaryContainer = Color(0xFFCBE6FF)

        // Surface
        internal val surface = Color(0xFF111318)
        internal val onSurface = Color(0xFFEDEDF5)
        internal val surfaceVariant = Color(0xFF37393E)
        internal val onSurfaceVariant = Color(0xFFC7C5D0)
        internal val inverseSurface = Color(0xFFE2E2E9)
        internal val inverseOnSurface = Color(0xFFC7C5D0)

        // Background
        internal val background = surface
        internal val onBackground = onSurface

        // Error
        internal val error = Color(0xFFF32525)
        internal val onError = Color(0xFFFFFFFF)
        internal val errorContainer = Color(0xFF73332D)
        internal val onErrorContainer = Color(0xFFFFDAD6)

        // Success
        internal val success = Color(0xFF11A644)
        internal val onSuccess = Color(0xFFFFFFFF)
        internal val successContainer = Color(0xFF093F1C)
        internal val onSuccessContainer = Color(0xFFD8FFDA)

        // Outline
        internal val outline = Color(0xFF91909A)
        internal val outlineVariant = Color(0xFF46464F)

        // Scrim
        internal val scrim = Color(0xFF000000)

        // Surface Container
        internal val surfaceDim = Color(0xFF111318)
        internal val surfaceBright = Color(0xFF37393E)
        internal val surfaceContainer = Color(0xFF1D2024)
        internal val surfaceContainerHigh = Color(0xFF282A2F)
        internal val surfaceContainerHighest = Color(0xFF33353A)
        internal val surfaceContainerLow = Color(0xFF191C20)
        internal val surfaceContainerLowest = Color(0xFF0C0E13)
    }
}
