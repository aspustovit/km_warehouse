package com.km.warehouse.ui.sync

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.km.warehouse.R
import org.koin.androidx.compose.koinViewModel

/**
 * Create by Pustovit Oleksandr on 1/5/2026
 */

@Composable
fun SyncDialog(
    onDismissRequest: () -> Unit,
    painter: Painter,
    imageDescription: String,
) {
    val viewModel: SyncViewModel = koinViewModel()
    val state = viewModel.viewState.collectAsState()

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(if(state.value.syncStatus != SyncStatus.ERROR) 375.dp else 475.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.sync_dialog),
                contentColor = colorResource(R.color.black)
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.sync_message),
                    modifier = Modifier.padding(16.dp),
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.width(32.dp))
                    Image(
                        painter = painterResource(R.drawable.ic_internet_connection_phone),
                        contentDescription = imageDescription,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.wrapContentSize()
                    )

                    when (state.value.syncStatus) {
                        SyncStatus.NOT_STARTED -> {
                            Spacer(
                                modifier = Modifier
                                    .height(78.dp)
                                    .width(140.dp)
                            )
                        }

                        SyncStatus.STARTED -> {
                            SyncLoader(
                                modifier = Modifier
                                    .height(78.dp)
                                    .width(140.dp)
                            )
                        }

                        SyncStatus.FINISHED -> {
                            SyncLoaderFinish(
                                modifier = Modifier
                                    .height(78.dp)
                                    .width(140.dp)
                            )
                        }

                        SyncStatus.ERROR -> {
                            Spacer(modifier = Modifier.width(12.dp))
                            Image(
                                painter = painterResource(R.drawable.ic_internet_connection_error),
                                contentDescription = imageDescription,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .height(78.dp)
                                    .width(140.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                    }

                    Image(
                        painter = painterResource(R.drawable.ic_internet_connection_internet),
                        contentDescription = imageDescription,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(78.dp)
                            .width(140.dp)
                    )
                }
                if(state.value.syncStatus == SyncStatus.ERROR) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = state.value.syncError!!,
                        fontSize = 11.sp,
                        color = colorResource(R.color.error)
                    )
                }
                Spacer(modifier = Modifier.height(48.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(text = stringResource(R.string.cancel), fontSize = 18.sp)
                    }
                    TextButton(
                        onClick = { viewModel.runSync() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.sync_data),
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SyncLoader(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.internet_connection_in_progress))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = { progress }
    )
}

@Composable
fun SyncLoaderFinish(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.internet_connection_success))
    val progress by animateLottieCompositionAsState(composition)
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = { progress }
    )
}