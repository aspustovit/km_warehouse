package com.km.warehouse.ui.inventory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.km.warehouse.R
import com.km.warehouse.domain.usecase.inventory.InventoryModel
import com.km.warehouse.ui.OutlinedIconButton

/**
 * Create by Pustovit Oleksandr on 01/06/2026
 */
@Composable
fun InventoryItemView(
    item: InventoryModel,
    idx: Int,
    onInventoryClick: (Pair<InventoryModel, Int>) -> Unit,
    setAsDoneClick: (InventoryModel) -> Unit
) {
    Spacer(modifier = Modifier.height(8.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable(onClick = { onInventoryClick(Pair(item, idx)) }),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            colorResource(R.color.sync_dialog),
            contentColor = colorResource(R.color.black)
        )
    ) {
        var headerIcon =
            if (item.freeQuantity == item.quantity) painterResource(id = R.drawable.ic_done_all)
            else painterResource(id = R.drawable.ic_star_border)
        var tintColor =
            if (item.factQuantity != 0.0) colorResource(R.color.finished_order) else colorResource(
                R.color.new_order
            )
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = headerIcon,
                contentDescription = null,
                tint = tintColor
            )
            /*Text(
                modifier = Modifier.padding(start = 8.dp),
                text = item.organizationName + ": ",
                fontSize = 16.sp
            )*/
            Text(
                modifier = Modifier.weight(1f, true),
                text = item.subInventoryCode,
                fontSize = 16.sp,
                color = colorResource(R.color.color_text_secondary)
            )
            OutlinedIconButton(
                resDrawable = R.drawable.ic_inventory_done,
                resDescription = R.string.inventory_set_as_done,
                onClick = { setAsDoneClick(item) }
            )
        }
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = item.mfgPartNumber,
                fontSize = 16.sp,
                color = colorResource(R.color.color_text_secondary)
            )
        }
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(text = item.itemDescription, fontSize = 12.sp, maxLines = 3)
            Spacer(modifier = Modifier.height(8.dp))
        }
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(text = stringResource(R.string.quantity), fontSize = 16.sp)
            Text(
                text = "${item.quantity}/${item.freeQuantity}${if (item.factQuantity == 0.0) "" else "/" + item.factQuantity}",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}