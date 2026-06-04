package com.km.warehouse.ui.inventory

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.km.warehouse.R
import com.km.warehouse.domain.usecase.inventory.InventoryModel
import com.km.warehouse.domain.usecase.inventory.UpdateInventoryModel
import com.km.warehouse.ui.CustomOutlinedImageButton
import com.km.warehouse.ui.DarkTopAppBar

/**
 * Create by Pustovit Oleksandr on 01/06/2026
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditInventoryView(
    item: InventoryModel,
    onBackClick: () -> Unit,
    onInventorySaveClick: (InventoryModel) -> Unit
) {
    val factQuantity = item.factQuantity.toInt()
    var fact by remember { mutableStateOf(if (factQuantity == 0) "" else factQuantity.toString()) }
    var comments by remember { mutableStateOf(item.comments) }

    BackHandler {
        onBackClick.invoke()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            DarkTopAppBar(
                title = { Text(stringResource(id = R.string.inventory)) },
                modifier = Modifier.fillMaxWidth(),
                actions = {
                }
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = CardDefaults.cardColors(
                    colorResource(R.color.sync_dialog),
                    contentColor = colorResource(R.color.black)
                )
            ) {
                var headerIcon = painterResource(id = R.drawable.ic_star_border)
                var tintColor =
                    if (item.quantity == item.freeQuantity) colorResource(R.color.finished_order) else colorResource(
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
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = item.organizationName + ": ",
                        fontSize = 16.sp
                    )
                    Text(
                        text = item.subInventoryCode,
                        fontSize = 16.sp,
                        color = colorResource(R.color.color_text_secondary)
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
                    Text(text = item.itemDescription, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(text = stringResource(R.string.quantity), fontSize = 16.sp)
                    Text(
                        text = "${item.quantity.toInt()}/${item.freeQuantity.toInt()}",
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = fact,
            onValueChange = { fact = it },
            label = { Text(stringResource(R.string.fact)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = comments,
            onValueChange = { comments = it },
            label = { Text(stringResource(R.string.comment)) },
            singleLine = true
        )

        CustomOutlinedImageButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            startIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_save),
                    contentDescription = null
                )
            },
            onClick = {
                item.factQuantity = if(fact.isNullOrEmpty()) 0.0 else fact.toDouble()
                item.comments = comments
                onInventorySaveClick.invoke(item)
            },
            enabled = true,
            text = {
                Text(
                    text = stringResource(id = R.string.save),
                    fontSize = 16.sp
                )
            })

    }
}