package com.km.warehouse.ui.sync

import com.km.warehouse.data.AppDataModule
import com.km.warehouse.data.network.entity.ErrorData
import java.net.URI

/**
 * Network failure recognised from the raw exception text stored in [ErrorData.message],
 * e.g. "java.net.SocketTimeoutException: failed to connect to /172.18.0.60 (port 8081) from ... after 10000ms".
 */
data class ConnectionError(
    val type: Type,
    /** Server address we tried to reach, e.g. "172.18.0.60:8081". */
    val serverAddress: String?,
    /** Connect timeout in seconds, when the exception reports it. */
    val timeoutSeconds: Long?,
    /** Original technical text, shown under "details". */
    val rawMessage: String
) {
    enum class Type { TIMEOUT, CONNECTION_REFUSED, UNKNOWN_HOST }

    companion object {
        // "connect to /172.18.0.60 (port 8081)" or "connect to /172.18.0.60:8081"
        private val CONNECT_TO_REGEX =
            Regex("""connect to /?([^\s/:()]+)(?::(\d+)|\s*\(port (\d+)\))?""", RegexOption.IGNORE_CASE)
        private val RESOLVE_HOST_REGEX =
            Regex("""Unable to resolve host "([^"]+)"""", RegexOption.IGNORE_CASE)
        private val AFTER_MS_REGEX = Regex("""after (\d+)\s*ms""", RegexOption.IGNORE_CASE)

        fun from(errorData: ErrorData?): ConnectionError? =
            errorData?.let { from("${it.error}\n${it.message}") }

        fun from(rawMessage: String?): ConnectionError? {
            if (rawMessage.isNullOrBlank()) return null
            val type = when {
                rawMessage.contains("SocketTimeoutException") ||
                        rawMessage.contains("InterruptedIOException: timeout") -> Type.TIMEOUT

                rawMessage.contains("UnknownHostException") -> Type.UNKNOWN_HOST

                rawMessage.contains("ConnectException") ||
                        rawMessage.contains("NoRouteToHostException") ||
                        rawMessage.contains("PortUnreachableException") -> Type.CONNECTION_REFUSED

                else -> return null
            }
            return ConnectionError(
                type = type,
                serverAddress = parseServerAddress(rawMessage) ?: defaultServerAddress(),
                timeoutSeconds = AFTER_MS_REGEX.find(rawMessage)
                    ?.groupValues?.get(1)?.toLongOrNull()?.div(1000),
                rawMessage = rawMessage.trim()
            )
        }

        private fun parseServerAddress(raw: String): String? {
            CONNECT_TO_REGEX.find(raw)?.let { match ->
                val host = match.groupValues[1]
                val port = match.groupValues[2].ifEmpty { match.groupValues[3] }
                return if (port.isEmpty()) host else "$host:$port"
            }
            return RESOLVE_HOST_REGEX.find(raw)?.groupValues?.get(1)
        }

        private fun defaultServerAddress(): String? = try {
            val uri = URI(AppDataModule.baseUrl)
            if (uri.port > 0) "${uri.host}:${uri.port}" else uri.host
        } catch (e: Exception) {
            null
        }
    }
}
