package com.km.warehouse.ui.utils

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import com.km.warehouse.R
import com.km.warehouse.domain.usecase.inventory.InventoryModel
import jxl.Workbook
import jxl.WorkbookSettings
import jxl.write.Label
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook
import java.io.File
import java.io.FileWriter
import java.util.Locale


/**
 * Create by Pustovit Oleksandr on 01/06/2026
 */
object ExcelExporter {
    const val XLS_SUBSTOCK_ID = 0
    const val XLS_CODE_ID = 1
    const val XLS_MANUFACTURE_ID = 2
    const val XLS_MODEL_ID = 3
    const val XLS_TOTAL_ID = 4
    const val XLS_FREE_ID = 5
    const val XLS_FACT_ID = 6
    const val XLS_COMMENTS_ID = 7

    fun Context.getXlsColumnHeader(): ArrayList<String> {
        val res = ArrayList<String>()
        res.add(getString(R.string.xls_substock))
        res.add(getString(R.string.xls_code))
        res.add(getString(R.string.xls_manufacture_id))
        res.add(getString(R.string.xls_model))
        res.add(getString(R.string.xls_total))
        res.add(getString(R.string.xls_free))
        res.add(getString(R.string.xls_fact))
        res.add(getString(R.string.xls_comments))
        return res
    }

    fun export(context: Context, inventory: List<InventoryModel>, barcode: String): Uri? {

        try {
            val sd: File = context.getExternalFilesDir(null)!!
            val csvFile = "inventory${barcode}.xls"

            val directory = File(sd.absolutePath)

            //create directory if not exist
            if (!directory.isDirectory()) {
                directory.mkdirs()
            }
            //file path

            val file = File(directory, csvFile)
            /*if(!file.exists()){
                Log.e("EXECEL", "${file.exists()}")
                file.createNewFile()
            }*/
            val wbSettings: WorkbookSettings = WorkbookSettings()
            wbSettings.locale = Locale(Locale.UK.language, Locale.UK.country)
            val workbook: WritableWorkbook = Workbook.createWorkbook(file, wbSettings)

            //Excel sheetA first sheetA
            val sheetA: WritableSheet = workbook.createSheet("${context.getString(R.string.xls_sheet)} $barcode", 0)
            // column and row titles
            context.getXlsColumnHeader().forEachIndexed { index, columnName ->
                sheetA.addCell(Label(index, 0, columnName))
            }
            inventory.forEachIndexed { index, model ->
                sheetA.addCell(Label(XLS_SUBSTOCK_ID,index+1 , model.subInventoryCode))
                sheetA.addCell(Label(XLS_CODE_ID,index+1 , model.itemSegment))
                sheetA.addCell(Label(XLS_MANUFACTURE_ID,index+1 , model.mfgPartNumber))
                sheetA.addCell(Label(XLS_MODEL_ID,index+1 , model.itemDescription))
                sheetA.addCell(Label(XLS_TOTAL_ID,index+1 , model.quantity.toString()))
                sheetA.addCell(Label(XLS_FREE_ID,index+1 , model.freeQuantity.toString()))
                sheetA.addCell(Label(XLS_FACT_ID,index+1 , model.factQuantity.toString()))
                sheetA.addCell(Label(XLS_COMMENTS_ID,index+1 , model.comments))
            }
            sheetA.setColumnView(XLS_MODEL_ID, 50)
            sheetA.setColumnView(XLS_MANUFACTURE_ID, 30)
            sheetA.setColumnView(XLS_COMMENTS_ID, 70)
            /*sheetA.addCell(Label(0, 0, "sheet A 1"))
            sheetA.addCell(Label(1, 0, "sheet A 2"))
            sheetA.addCell(Label(0, 1, "sheet A 3"))
            sheetA.addCell(Label(1, 1, "sheet A 4"))*/

            //Excel sheetB represents second sheet
            /*val sheetB: WritableSheet = workbook.createSheet("sheet B", 1)

            // column and row titles
            sheetB.addCell(Label(0, 0, "sheet B 1"))
            sheetB.addCell(Label(1, 0, "sheet B 2"))
            sheetB.addCell(Label(0, 1, "sheet B 3"))
            sheetB.addCell(Label(1, 1, "sheet B 4"))*/

            // close workbook
            workbook.write()
            workbook.close()
            val uri =
                FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
            return uri

        } catch (e: Exception) {
            Log.e("EXECEL", "$e")
            e.printStackTrace()
            return null
        }
    }

    fun createTestFile(context: Context): Uri? {
        try {
            val dir = context.cacheDir
            if (!dir.exists())
                dir.mkdirs()

            val fileDeviceInfo =
                File(context.cacheDir, "cojail_file.txt")
            if (!fileDeviceInfo.exists())
                fileDeviceInfo.createNewFile()

            val out = FileWriter(fileDeviceInfo)
            out.write("Hellow txt file Cojail!\n")
            out.close()

            if (fileDeviceInfo.exists() && fileDeviceInfo.canRead()) {
                val att = FileProvider.getUriForFile(
                    context,
                    context.packageName + ".fileprovider",
                    fileDeviceInfo
                )
                return att
            }
        } catch (ex: Exception) {
            Log.e("CojailSender_У", "${ex}")
            return null
        }
        return null
    }

    fun getIntentForMail(
        mailRecipient: String? = null,
        subject: String,
        text: String? = null,
        uri: Uri? = null,
        context: Context
    ): Intent {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.setType("message/rfc822") //use this line for testing on the real phone
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String?>(mailRecipient))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, text)

        if (uri != null) {
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri)
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            emailIntent.clipData = ClipData.newUri(context.contentResolver, "Attachment", uri)
        }
        Log.e("EXECEL", "$emailIntent")
        return emailIntent
    }

}