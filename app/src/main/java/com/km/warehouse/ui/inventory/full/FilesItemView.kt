package com.km.warehouse.ui.inventory.full

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.km.warehouse.R
import com.km.warehouse.domain.usecase.inventory.InventoryFileModel
import com.km.warehouse.ui.inventory.FileDropdownMenu
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

/**
 * Create by Pustovit Oleksandr on 09/06/2026
 */

val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

@Composable
fun FilesItemView(
    onFileClick: (InventoryFileModel) -> Unit,
    item: InventoryFileModel,
    onEdit: (InventoryFileModel) -> Unit,
    onDelete: (InventoryFileModel) -> Unit,
    onExel: (InventoryFileModel) -> Unit
) {
    Spacer(modifier = Modifier.height(8.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable(onClick = { onFileClick(item) }),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            colorResource(R.color.sync_dialog),
            contentColor = colorResource(R.color.black)
        )
    ) {
        var headerIcon = painterResource(id = R.drawable.ic_xsl)
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = headerIcon,
                contentDescription = null/*,
                tint = tintColor*/
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(R.string.xls_file_date),
                fontSize = 16.sp
            )

            Text(
                modifier = Modifier.weight(1f, true),
                text = formatter.format(Date(item.time)),
                fontSize = 16.sp,
                color = colorResource(R.color.color_text_secondary)
            )
            FileDropdownMenu(onExel = {onExel(item)}, onEdit = { onEdit(item) }, onDelete = { onDelete(item) })
        }

        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            Text(
                text = item.fileName,
                fontSize = 20.sp,
                color = colorResource(R.color.black)
            )
        }
    }
}