package com.km.warehouse

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import android.view.KeyEvent.KEYCODE_SHIFT_LEFT
import android.view.KeyEvent.KEYCODE_SHIFT_RIGHT
import android.view.KeyEvent.KEYCODE_TV_ANTENNA_CABLE
import android.view.KeyEvent.KEYCODE_TV_SATELLITE_SERVICE
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fieldbee.core.ui.compose.utils.getAppVersionName
import com.km.warehouse.ui.CustomOutlinedImageButton
import com.km.warehouse.ui.DarkTopAppBar
import com.km.warehouse.ui.SharedViewModel
import com.km.warehouse.ui.sync.ErrorDialog
import com.km.warehouse.ui.sync.SyncDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import kotlin.experimental.and

class MainActivity : ComponentActivity() {
    val viewModel: SharedViewModel by viewModel()
    val supportedSymbols : CharArray = charArrayOf('-','/',':',';','_','(',')','*','&','%','#','!','?','|','\\','[',']','{','}')

    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null
    private var writeMode = false
    private var dataToWrite: String = ""
    private lateinit var writeTagFilters: Array<IntentFilter>

    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(e: KeyEvent): Boolean {
        Log.d("NAV_CONTROLLER", "$e")
        if (e.action == KeyEvent.ACTION_DOWN && e.keyCode != KEYCODE_TV_SATELLITE_SERVICE
            && e.keyCode != KEYCODE_SHIFT_LEFT && e.keyCode != KEYCODE_SHIFT_RIGHT
            && e.keyCode != KEYCODE_BACK && e.keyCode != KEYCODE_TV_ANTENNA_CABLE) {
            val pressedKey = e.unicodeChar.toChar()
            Log.i("NAV_CONTROLLER", "$pressedKey")
            if (e.keyCode != KeyEvent.KEYCODE_ENTER && Character.isLetterOrDigit(pressedKey) || supportedSymbols.contains(pressedKey))
                viewModel.onBarcodeScan(pressedKey)
        }
        if (e.action == KeyEvent.ACTION_DOWN && e.keyCode == KeyEvent.KEYCODE_ENTER) {
            viewModel.postBarcode()
            return false
        }
        return super.dispatchKeyEvent(e)
    }

    override fun onNewIntent(intent: Intent) {
        Log.d("NFC", "New INTENT arrived = $writeMode")

        super.onNewIntent(intent)
        setIntent(intent)
        /*if (writeMode)
            writeToTagFromIntent(intent)
        else*/
            readFromIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        //For when the activity is launched by the intent-filter for android.nfc.action.NDEF_DISCOVER
        readFromIntent(intent)
        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT)
        writeTagFilters = arrayOf(tagDetected)

        setContent {
            MaterialTheme { // Apply your app's theme
                NavigationController(modifier = Modifier.fillMaxSize()) // Call your screen's composable
                if (nfcAdapter == null) {
                    ErrorDialog(errorMessage = "This device doesn't support NFC.") {}
                }
            }
        }
    }

    /******************************************************************************
     * Read From NFC Tag
     ****************************************************************************/
    private fun readFromIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action || NfcAdapter.ACTION_TECH_DISCOVERED == action || NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val rawMessages: Array<Parcelable>? = intent.parcelableArray(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val messages = mutableListOf<NdefMessage>()
            if (rawMessages != null) {
                for (i in rawMessages.indices) {
                    messages.add(i, rawMessages[i] as NdefMessage)
                }
                buildTagViews(messages.toTypedArray())
            }
        }
    }

    private fun buildTagViews(messages: Array<NdefMessage>) {
        if (messages.isEmpty()) return
        val payload = messages[0].records[0].payload
        val textEncoding: Charset =
            if ((payload[0] and 128.toByte()).toInt() == 0) Charsets.UTF_8 else Charsets.UTF_16 // Get the Text Encoding
        val languageCodeLength: Int =
            (payload[0] and 51).toInt() // Get the Language Code, e.g. "en"
        try {
            // Get the Text
            val text = String(
                payload,
                languageCodeLength + 1,
                payload.size - languageCodeLength - 1,
                textEncoding
            )
            Log.i("NFC", "Message read from NFC Tag:\n $text")
            viewModel.addBarcodeFromNFC(text)
            viewModel.postBarcode()
        } catch (e: UnsupportedEncodingException) {
            Log.e("UnsupportedEncoding", e.toString())
        }
    }

    public override fun onPause() {
        super.onPause()
        nfcAdapter!!.disableForegroundDispatch(this)
    }

    public override fun onResume() {
        super.onResume()
        checkNfcEnabled()
        nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null)
    }

    private fun checkNfcEnabled() {
        if (nfcAdapter != null) {
            val nfcEnabled: Boolean = nfcAdapter!!.isEnabled
            if (!nfcEnabled) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.text_warning_nfc_is_off))
                    .setMessage(getString(R.string.text_turn_on_nfc))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.text_update_settings)
                    ) { _, _ -> startActivity(Intent(Settings.ACTION_NFC_SETTINGS)) }
                    .create().show()
            }
        }
    }

    private inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }

    private fun Intent.parcelableArray(key: String): Array<Parcelable>? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableArrayExtra(key, Parcelable::class.java)
        else -> @Suppress("DEPRECATION") (getParcelableArrayExtra(key) as Array<Parcelable>?)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    activity: ComponentActivity,
    onApprovedDocumentClick: () -> Unit = {},
    onIssuedDocumentClick: () -> Unit = {},
    onIncomeDocumentClick: () -> Unit = {},
    onScanToFileClick: () -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        SyncDialog(
            onDismissRequest = {
                showDialog = false
            },
            painter = painterResource(R.drawable.ic_internet_connection_phone),
            imageDescription = ""
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DarkTopAppBar(
            title = { Text(stringResource(id = R.string.main_screen_title)) },
            modifier = Modifier.fillMaxWidth()
        )
        CustomOutlinedImageButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            startIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_barcode_scanner),
                    contentDescription = null
                )
            },
            onClick = { onApprovedDocumentClick.invoke() },
            enabled = true,
            text = {
                Text(
                    text = stringResource(id = R.string.approved_documents),
                    fontSize = 16.sp
                )
            })

        CustomOutlinedImageButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            startIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_document),
                    contentDescription = null
                )
            },
            onClick = {
                onIssuedDocumentClick.invoke()
            },
            enabled = true,
            text = {
                Text(
                    text = stringResource(id = R.string.issued_documents),
                    fontSize = 16.sp
                )
            })

        CustomOutlinedImageButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            startIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_settings),
                    contentDescription = null
                )
            },
            onClick = {
                onIncomeDocumentClick.invoke()
            },
            enabled = true,
            text = {
                Text(
                    text = stringResource(id = R.string.settings),
                    fontSize = 16.sp
                )
            })

        CustomOutlinedImageButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            startIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_barcode_reader),
                    contentDescription = null
                )
            },
            onClick = {
                onScanToFileClick.invoke()
            },
            enabled = true,
            text = {
                Text(
                    text = stringResource(id = R.string.scan_to_file),
                    fontSize = 16.sp
                )
            })

        CustomOutlinedImageButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            startIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_cloud_sync),
                    contentDescription = null
                )

            },
            onClick = {
                showDialog = true
            },
            enabled = true,
            text = {
                Text(
                    text = stringResource(id = R.string.sync),
                    fontSize = 16.sp
                )
            })

        CustomOutlinedImageButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            startIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = null
                )
            },
            onClick = {
                activity.finish()
            },
            enabled = true,
            text = {
                Text(
                    text = stringResource(id = R.string.exit),
                    fontSize = 16.sp
                )
            })

        Text(modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp), text = "${stringResource(id = R.string.version)} ${activity.getAppVersionName()}" )
        //
        // Add more UI elements here, like Buttons, Images, etc.
    }

}