package com.km.warehouse.ui.move_order

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.km.warehouse.R
import com.km.warehouse.domain.usecase.model.ItemSerialModel
import com.km.warehouse.domain.usecase.model.MoveOrderItemsModel

/**
 * Create by Pustovit Oleksandr on 2/5/2026
 */

@Composable
fun MoveOrderItemView(
    item: MoveOrderItemsModel,
    serials: List<ItemSerialModel>,
    orderItemForScan: MoveOrderItemsModel? = null,
    onDeleteSerial: (ItemSerialModel) -> Unit,
    showDeleteBtn: Boolean = false,
    onSelectMoveOrderItem: (MoveOrderItemsModel) -> Unit,
) {
    var qtyGiven= item.qtyGiven.toInt()
    var color = if (orderItemForScan != null && orderItemForScan.id == item.id) {
        qtyGiven = orderItemForScan.qtyGiven.toInt()
        colorResource(R.color.move_order_complete)
    } else {
        colorResource(R.color.sync_dialog)
    }
    var headerIcon = painterResource(id = R.drawable.ic_not_start_order)
    var tintColor = colorResource(R.color.new_order)
    val serialsList = serials.filter { it.moveOrderItemId == item.id }

    if(item.qtyGiven == item.quantity && item.noSerials) {
        headerIcon = painterResource(id = R.drawable.ic_order_finished)
        tintColor = colorResource(R.color.finished_order)
    } else if(item.qtyGiven == item.quantity && serialsList.size == item.qtyGiven.toInt()) {
        headerIcon = painterResource(id = R.drawable.ic_order_finished)
        tintColor= colorResource(R.color.finished_order)
    } else if(item.qtyGiven == item.quantity && serialsList.size != item.qtyGiven.toInt()){
        headerIcon = painterResource(id = R.drawable.ic_order_not_finished)
        tintColor= colorResource(R.color.not_finished_order)
    } else {
        headerIcon = painterResource(id = R.drawable.ic_not_start_order)
        tintColor= colorResource(R.color.new_order)
    }
    val serialsCountState = if(item.noSerials) "-" else "${serialsList.size}"
    Spacer(modifier = Modifier.height(8.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                onSelectMoveOrderItem(item)
            })
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color,
            contentColor = colorResource(R.color.black)
        )
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(modifier = Modifier.padding(8.dp),
                painter = headerIcon,
                contentDescription = null,
                tint = tintColor
            )
            Text(text = stringResource(R.string.code), fontSize = 16.sp)
            Text(
                text = item.mfrCode,
                fontSize = 16.sp,
                color = colorResource(R.color.color_text_secondary)
            )
        }
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(text = stringResource(R.string.item_segment), fontSize = 16.sp)
            Text(
                text = item.itemSegment ?: "",
                fontSize = 16.sp,
                color = colorResource(R.color.color_text_secondary)
            )
        }
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(text = item.description, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(text = stringResource(R.string.quantity), fontSize = 16.sp)
            Text(text = "${item.quantity.toInt()}/${qtyGiven}/${serialsCountState}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }
        // Spacer(modifier = Modifier.height(8.dp))
        Log.i("SERIALS", "${item.moveOrderId} = list $serials")
        serialsList.forEach { serial ->
            Log.e("_SERIALS_", "Upadte list $serials")
            Column {
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(colorResource(R.color.color_text_secondary))
                )
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        text = serial.serial,
                        fontSize = 14.sp,
                        color = colorResource(R.color.color_text_secondary),
                        fontWeight = FontWeight.Bold
                    )
                    if (showDeleteBtn) {
                        IconButton(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .size(24.dp),
                            onClick = {
                                onDeleteSerial(serial)
                            }) {
                            Icon(
                                painterResource(id = R.drawable.ic_delete),
                                contentDescription = null
                            )
                        }
                    }
                }

            }

        }
    }
}
