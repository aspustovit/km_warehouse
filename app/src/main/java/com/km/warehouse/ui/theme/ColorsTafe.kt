package com.fieldbee.core.ui.compose.theme

import androidx.compose.ui.graphics.Color

internal object ColorsTafe {
    /**
     * Light TAFE theme colors
     */
    internal object Light {

        // Primary
        internal val primary = Color(0xFFF3702A)
        internal val onPrimary = Color(0xFF333333)
        internal val primaryContainer = Color(0xFFEDE1E2)
        internal val onPrimaryContainer = Color(0xFFE69D77)
        internal val inversePrimary = Color(0xFFF3702A)

        // Secondary
        internal val secondary = Color(0xFF333333)
        internal val onSecondary = Color(0xFFFFFFFF)
        internal val secondaryContainer = Color(0xFFE1E1EA)
        internal val onSecondaryContainer = Color(0xFF191C20)

        // Tertiary
        internal val tertiary = Color(0xFF29638A)
        internal val onTertiary = Color(0xFFFFFFFF)
        internal val tertiaryContainer = Color(0xFFCBE6FF)
        internal val onTertiaryContainer = Color(0xFF001E30)

        // Surface
        internal val surface = Color(0xFFFDFDFF)
        internal val onSurface = Color(0xFF181819)
        internal val surfaceVariant = Color(0xFFF1F1F7)
        internal val onSurfaceVariant = Color(0xFF616168)
        internal val inverseSurface = Color(0xFF202122)
        internal val inverseOnSurface = Color(0xFFF0F0F7)

        // Background
        internal val background = surface
        internal val onBackground = onSurface

        // Error
        internal val error = Color(0xFFE41717)
        internal val onError = Color(0xFFFFFFFF)
        internal val errorContainer = Color(0xFFFFE8E8)
        internal val onErrorContainer = Color(0xFF980E0E)

        // Success
        internal val success = Color(0xFF11A644)
        internal val onSuccess = Color(0xFFFFFFFF)
        internal val successContainer = Color(0xFFD8FFDA)
        internal val onSuccessContainer = Color(0xFF0A6229)

        // Outline
        internal val outline = Color(0xFF8E8E97)
        internal val outlineVariant = Color(0xFFD1D2DD)

        // Scrim
        internal val scrim = Color(0xFF000000)

        // Surface Container
        internal val surfaceDim = Color(0xFFE6E6EC)
        internal val surfaceBright = Color(0xFFFDFDFF)
        internal val surfaceContainer = Color(0xFFF1F1F8)
        internal val surfaceContainerHigh = Color(0xFFECECF4)
        internal val surfaceContainerHighest = Color(0xFFE6E6F1)
        internal val surfaceContainerLow = Color(0xFFF7F7FD)
        internal val surfaceContainerLowest = Color(0xFFFFFFFF)
    }

    /**
     * Dark TAFE theme colors
     */
    internal object Dark {

        // Primary
        internal val primary = Color(0xFFF3702A)
        internal val onPrimary = Color(0xFF111318)
        internal val primaryContainer = Color(0xFF2C2E33)
        internal val onPrimaryContainer = Color(0xFF8C8C8C)
        internal val inversePrimary = Color(0xFF171615)

        // Secondary
        internal val secondary = Color(0xFFFFFFFF)
        internal val onSecondary = Color(0xFF0C0E13)
        internal val secondaryContainer = Color(0xFF2D3036)
        internal val onSecondaryContainer = Color(0xFFF3702A)

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