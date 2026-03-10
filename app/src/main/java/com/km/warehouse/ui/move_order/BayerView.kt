package com.km.warehouse.ui.move_order

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.km.warehouse.R

/**
 * Create by Pustovit Oleksandr on 2/23/2026
 */
@Composable
fun BayerView(modifier: Modifier, showExpand: Boolean, isExpand: Boolean, key: String) {
    Row(modifier = modifier) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            text = key,
            color = colorResource(R.color.black),
            fontSize = 18.sp,
            fontFamily = FontFamily.SansSerif
        )
        if (showExpand) {
            Icon(
                painter = painterResource(id = if (isExpand) R.drawable.ic_expand else R.drawable.ic_expand_more),
                contentDescription = null, modifier = Modifier.weight(weight = 0.2f)
            )
        }
    }
}

@Composable
fun BayerViewSmall(modifier: Modifier, showExpand: Boolean, isExpand: Boolean, key: String) {
    Row(modifier = modifier) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            text = key,
            color = colorResource(R.color.black),
            fontSize = 14.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold
        )
        if (showExpand) {
            Icon(
                painter = painterResource(id = if (isExpand) R.drawable.ic_expand else R.drawable.ic_expand_more),
                contentDescription = null, modifier = Modifier.weight(weight = 0.2f)
            )
        }
    }
}