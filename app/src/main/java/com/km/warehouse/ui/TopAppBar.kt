package com.km.warehouse.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/**
 * A customizable top app bar with a dark theme that adapts to scrolling and includes slots for a
 * title, navigation icon, and action items.
 *
 * @param title a composable function that defines the content of the title. Typically a Text composable.
 * @param modifier the [Modifier] to be applied to this top app bar.
 * @param navigationIcon a composable function that defines the content of the navigation icon.
 * Defaults to an empty function.
 * @param actions a composable function that defines the content of the action items, placed at the
 * end of the top app bar. Defaults to an empty function.
 * @param expandedHeight the height of the top app bar when it is expanded. Defaults to
 * [TopAppBarDefaults.TopAppBarExpandedHeight].
 * @param windowInsets the window insets to be applied to this top app bar. Defaults to
 * [TopAppBarDefaults.windowInsets].
 * @param scrollBehavior optional [TopAppBarScrollBehavior] to handle scroll effects like collapsing
 * the top app bar when the content is scrolled. Defaults to `null`.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DarkTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable() (RowScope.() -> Unit) = {},
    expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        expandedHeight = expandedHeight,
        windowInsets = windowInsets,
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.onSecondary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
            navigationIconContentColor = MaterialTheme.colorScheme.onSecondary
        ),
        scrollBehavior = scrollBehavior
    )
}