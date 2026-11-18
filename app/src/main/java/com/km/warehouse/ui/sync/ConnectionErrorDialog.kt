package com.km.warehouse.ui.sync

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fieldbee.core.ui.compose.utils.playSound
import com.km.warehouse.R

/**
 * Material 3 dialog shown when the server can't be reached.
 */
@Composable
fun ConnectionErrorDialog(
    connectionError: ConnectionError,
    onDismiss: () -> Unit,
    onRetry: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    LaunchedEffect(connectionError) {
        if (!isPreview) context.playSound(R.raw.windows_error)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Image(
                painter = painterResource(R.drawable.ic_internet_connection_error),
                contentDescription = null,
                modifier = Modifier.height(40.dp)
            )
        },
        title = {
            Text(
                text = stringResource(R.string.connection_error_title),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = { ConnectionErrorContent(connectionError) },
        confirmButton = {
            if (onRetry != null) {
                TextButton(onClick = onRetry) {
                    Text(stringResource(R.string.connection_error_retry))
                }
            } else {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.ok))
                }
            }
        },
        dismissButton = if (onRetry != null) {
            { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } }
        } else null,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        iconContentColor = MaterialTheme.colorScheme.error,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

/**
 * Body of [ConnectionErrorDialog]; also usable inline (e.g. inside the sync card).
 */
@Composable
fun ConnectionErrorContent(
    connectionError: ConnectionError,
    modifier: Modifier = Modifier
) {
    var showDetails by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 360.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(
                when (connectionError.type) {
                    ConnectionError.Type.TIMEOUT -> R.string.connection_error_timeout
                    ConnectionError.Type.CONNECTION_REFUSED -> R.string.connection_error_refused
                    ConnectionError.Type.UNKNOWN_HOST -> R.string.connection_error_unknown_host
                }
            ),
            style = MaterialTheme.typography.bodyLarge
        )

        connectionError.serverAddress?.let { address ->
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Text(
                        text = stringResource(R.string.connection_error_server_address),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = address,
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    connectionError.timeoutSeconds?.let { seconds ->
                        Text(
                            text = stringResource(R.string.connection_error_waited, seconds),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Text(
            text = stringResource(R.string.connection_error_hint),
            style = MaterialTheme.typography.bodyMedium
        )

        TextButton(
            onClick = { showDetails = !showDetails },
            modifier = Modifier.padding(0.dp)
        ) {
            Text(
                stringResource(
                    if (showDetails) R.string.connection_error_hide_details
                    else R.string.connection_error_show_details
                )
            )
        }
        AnimatedVisibility(visible = showDetails) {
            Text(
                text = connectionError.rawMessage,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

// region Previews

private class ConnectionErrorPreviewProvider : PreviewParameterProvider<ConnectionError> {
    override val values = sequenceOf(
        ConnectionError(
            type = ConnectionError.Type.TIMEOUT,
            serverAddress = "172.18.0.60:8081",
            timeoutSeconds = 10,
            rawMessage = "600 - AuthRepositoryImpl.login()\n java.net.SocketTimeoutException: " +
                    "failed to connect to /172.18.0.60 (port 8081) from /172.16.6.147 (port 41268) after 10000ms"
        ),
        ConnectionError(
            type = ConnectionError.Type.CONNECTION_REFUSED,
            serverAddress = "172.18.0.60:8081",
            timeoutSeconds = null,
            rawMessage = "600 - syncWarehouseData\n java.net.ConnectException: " +
                    "Failed to connect to /172.18.0.60:8081"
        ),
        ConnectionError(
            type = ConnectionError.Type.UNKNOWN_HOST,
            serverAddress = "warehouse.km.local",
            timeoutSeconds = null,
            rawMessage = "600 - LOG_IN_FAIL\n java.net.UnknownHostException: " +
                    "Unable to resolve host \"warehouse.km.local\": No address associated with hostname"
        )
    )
}

@Preview(name = "Connection error", showBackground = true, locale = "uk")
@Composable
private fun ConnectionErrorDialogPreview(
    @PreviewParameter(ConnectionErrorPreviewProvider::class) connectionError: ConnectionError
) {
    MaterialTheme {
        ConnectionErrorDialog(connectionError = connectionError, onDismiss = {})
    }
}

@Preview(name = "Connection error with retry", showBackground = true, locale = "uk")
@Composable
private fun ConnectionErrorDialogRetryPreview() {
    MaterialTheme {
        ConnectionErrorDialog(
            connectionError = ConnectionErrorPreviewProvider().values.first(),
            onDismiss = {},
            onRetry = {}
        )
    }
}

@Preview(name = "Inline content (sync card)", showBackground = true, widthDp = 360, locale = "uk")
@Composable
private fun ConnectionErrorContentPreview() {
    MaterialTheme {
        Surface {
            ConnectionErrorContent(
                connectionError = ConnectionErrorPreviewProvider().values.first(),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

// endregion
