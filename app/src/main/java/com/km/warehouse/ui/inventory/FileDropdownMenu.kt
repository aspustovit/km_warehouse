package com.km.warehouse.ui.inventory

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.km.warehouse.R

/**
 * Create by Pustovit Oleksandr on 17/06/2026
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileDropdownMenu(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onExel: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { isMenuExpanded = true }) {
            Icon(
                painter = painterResource(R.drawable.ic_menu),
                contentDescription = "Open action menu"
            )
        }

        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.rename)) },
                onClick = {
                    isMenuExpanded = false
                    onEdit()
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete)) },
                onClick = {
                    isMenuExpanded = false
                    onDelete()
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.to_exel)) },
                onClick = {
                    isMenuExpanded = false
                    onExel()
                }
            )
        }
    }
}