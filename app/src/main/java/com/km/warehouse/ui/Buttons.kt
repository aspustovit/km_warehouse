package com.km.warehouse.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

/**
 * Create by Pustovit Oleksandr on 9/17/2025
 */

/**
 * Field Custom filled button with generic content slot. Wraps Material 3 [Button].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param contentPadding The spacing values to apply internally between the container and the
 * content.
 * @param content The button content.
 */
@Composable
fun CustomButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = contentPadding(),
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(
            minWidth = CustomButtonDefaults.minWidth(),
            minHeight = CustomButtonDefaults.minHeight()
        ),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        contentPadding = contentPadding,
        content = content,
    )
}

/**
 * Field Custom filled button with text and icon content slots.
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param text The button text label content.
 * @param startIcon The button start icon content. Pass `null` here for no start icon.
 * @param endIcon The button end icon content. Pass `null` here for no end icon.
 */
@Composable
fun CustomButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    startIcon: @Composable (() -> Unit)? = null,
    endIcon: @Composable (() -> Unit)? = null,
) {
    CustomButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding(startIcon != null, endIcon != null),
    ) {
        CustomButtonContent(
            text = text,
            startIcon = startIcon,
            endIcon = endIcon,
        )
    }
}

/**
 * Field Custom outlined button with generic content slot. Wraps Material 3 [OutlinedButton].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param contentPadding The spacing values to apply internally between the container and the
 * content.
 * @param content The button content.
 */
@Composable
fun CustomOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.outline) {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.defaultMinSize(
                minWidth = CustomButtonDefaults.minWidth(),
                minHeight = CustomButtonDefaults.minHeight()
            ),
            enabled = enabled,
            border = IconButtonDefaults.outlinedIconButtonBorder(enabled = enabled),
            contentPadding = contentPadding,
            content = content,
        )
    }
}

@Composable
fun OutlinedIconButton(
    onClick: () -> Unit,
    resDrawable: Int,
    resDescription: Int
) {
    OutlinedIconButton(
        onClick = { onClick() },
    ) {
        val imageVector = ImageVector.vectorResource(id = resDrawable)

        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(resDescription)
        )
    }
}

/**
 * Field Custom outlined button with text and icon content slots.
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param text The button text label content.
 * @param startIcon The button start icon content. Pass `null` here for no start icon.
 * @param endIcon The button end icon content. Pass `null` here for no end icon.
 */
@Composable
fun CustomOutlinedImageButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    startIcon: @Composable (() -> Unit)? = null,
    endIcon: @Composable (() -> Unit)? = null,
) {
    CustomOutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding(startIcon != null, endIcon != null)
    ) {
        CustomButtonContent(
            text = text,
            startIcon = startIcon,
            endIcon = endIcon,
        )
    }
}

/**
 * Field Custom elevated button with generic content slot. Wraps Material 3 [ElevatedButton].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param contentPadding The spacing values to apply internally between the container and the
 * content.
 * @param content The button content.
 */
@Composable
fun CustomElevatedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(
            minWidth = CustomButtonDefaults.minWidth(),
            minHeight = CustomButtonDefaults.minHeight()
        ),
        enabled = enabled,
        contentPadding = contentPadding,
        content = content,
    )
}

/**
 * Field Custom elevated button with text and icon content slots.
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param text The button text label content.
 * @param startIcon The button start icon content. Pass `null` here for no start icon.
 * @param endIcon The button end icon content. Pass `null` here for no end icon.
 */
@Composable
fun CustomElevatedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    startIcon: @Composable (() -> Unit)? = null,
    endIcon: @Composable (() -> Unit)? = null,
) {
    CustomElevatedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding(startIcon != null, endIcon != null)
    ) {
        CustomButtonContent(
            text = text,
            startIcon = startIcon,
            endIcon = endIcon,
        )
    }
}

/**
 * Field Custom tonal button with generic content slot. Wraps Material 3 [FilledTonalButton].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param contentPadding The spacing values to apply internally between the container and the
 * content.
 * @param content The button content.
 */
@Composable
fun CustomTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = contentPadding(),
    content: @Composable RowScope.() -> Unit,
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(
            minWidth = CustomButtonDefaults.minWidth(),
            minHeight = CustomButtonDefaults.minHeight()
        ),
        enabled = enabled,
        contentPadding = contentPadding,
        content = content,
    )
}

/**
 * Field Custom tonal button with text and icon content slots.
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param text The button text label content.
 * @param startIcon The button start icon content. Pass `null` here for no start icon.
 * @param endIcon The button end icon content. Pass `null` here for no end icon.
 */
@Composable
fun CustomTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    startIcon: @Composable (() -> Unit)? = null,
    endIcon: @Composable (() -> Unit)? = null,
) {
    CustomTonalButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding(startIcon != null, endIcon != null),
    ) {
        CustomButtonContent(
            text = text,
            startIcon = startIcon,
            endIcon = endIcon,
        )
    }
}

/**
 * Field Custom text button with generic content slot. Wraps Material 3 [Button].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param contentPadding The spacing values to apply internally between the container and the
 * content.
 * @param content The button content.
 */
@Composable
fun CustomTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = contentPadding(),
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(
            minWidth = CustomButtonDefaults.minWidth(),
            minHeight = CustomButtonDefaults.minHeight()
        ),
        enabled = enabled,
        contentPadding = contentPadding,
        content = content,
    )
}

/**
 * Field Custom text button with text and icon content slots.
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param text The button text label content.
 * @param startIcon The button start icon content. Pass `null` here for no start icon.
 * @param endIcon The button end icon content. Pass `null` here for no end icon.
 */
@Composable
fun CustomTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    startIcon: @Composable (() -> Unit)? = null,
    endIcon: @Composable (() -> Unit)? = null,
) {
    CustomTextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding(startIcon != null, endIcon != null),
    ) {
        CustomButtonContent(
            text = text,
            startIcon = startIcon,
            endIcon = endIcon,
        )
    }
}

/**
 * Internal Field Custom button content layout for arranging the text label and leading icon.
 *
 * @param text The button text label content.
 * @param startIcon The button left icon content. Default is `null`
 * @param endIcon The button right icon content. Default is `null`
 */
@Composable
private fun CustomButtonContent(
    text: @Composable () -> Unit,
    startIcon: @Composable (() -> Unit)? = null,
    endIcon: @Composable (() -> Unit)? = null,
) {
    if (startIcon != null) {
        Box(
            modifier = Modifier.size(CustomButtonDefaults.iconSize()),
            propagateMinConstraints = true
        ) {
            startIcon()
        }
    }
    Box(
        modifier = Modifier
            .padding(
                start = if (startIcon != null) CustomButtonDefaults.iconSpacing() else 0.dp,
                end = if (endIcon != null) CustomButtonDefaults.iconSpacing() else 0.dp,
            )
    ) {
        text()
    }
    if (endIcon != null) {
        Box(
            modifier = Modifier.size(CustomButtonDefaults.iconSize()),
            propagateMinConstraints = true
        ) {
            endIcon()
        }
    }
}

/**
 * The default content padding used by [Button] that contains an [Icon].
 */
@Stable
@Composable
private fun contentPadding(start: Boolean = false, end: Boolean = false): PaddingValues =
    PaddingValues(
        start = if (start) CustomButtonDefaults.buttonWithIconHorizontalPadding() else CustomButtonDefaults.buttonHorizontalPadding(),
        top = CustomButtonDefaults.ButtonVerticalPadding,
        end = if (end) CustomButtonDefaults.buttonWithIconHorizontalPadding() else CustomButtonDefaults.buttonHorizontalPadding(),
        bottom = CustomButtonDefaults.ButtonVerticalPadding
    )

private object CustomButtonDefaults {

    @Composable
    fun minWidth() = 56.dp

    @Composable
    fun minHeight() = 40.dp

    @Composable
    fun iconSpacing() = 8.dp

    @Composable
    fun iconSize() = 18.dp

    @Composable
    fun buttonHorizontalPadding() = 24.dp

    @Composable
    fun buttonWithIconHorizontalPadding() = 16.dp

    val ButtonVerticalPadding = 8.dp

}
