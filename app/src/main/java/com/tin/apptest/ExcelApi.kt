package com.tin.apptest

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import jxl.Sheet
import jxl.Workbook
import jxl.write.WritableWorkbook
import java.io.File
import java.io.FileNotFoundException

class ExcelApi(private val context: Context) {
    val TAG = "APP_TEST"
    private val mainDataDir = File(Environment.getExternalStorageDirectory(), "Test")
    private val xlsFile = "myExcel.xls"

    private var wb: Workbook? = null
    private var writeWb: WritableWorkbook? = null

    // Kotlin Initializer block for the class.
    init{
        var dirExists: Boolean = false

        if(!mainDataDir.exists()){
            dirExists = mainDataDir.mkdirs()

            if(!dirExists){
                Toast.makeText(context,"Fail to create folder!",Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun openXls() : Boolean{
        val inFile = File(mainDataDir, xlsFile)

        return try {
            wb = Workbook.getWorkbook(inFile)
            writeWb = Workbook.createWorkbook(inFile,wb)
            true
        }catch (e: FileNotFoundException){
            writeWb = Workbook.createWorkbook(inFile)
            true
        }catch (ex:Exception){
            Log.e(TAG, ex.message.toString())
            Toast.makeText(context,ex.message.toString(), Toast.LENGTH_LONG).show()
            false
        }
    }

    fun closeXls(){
        try {
            writeWb?.write()
            writeWb?.close()

            wb?.close()
        }catch(e:Exception){
            Log.e(TAG, e.message.toString())
            Toast.makeText(context,e.message.toString(),Toast.LENGTH_LONG).show()
        }
    }

    fun getRowsCount(): Int?{
        openXls()
        val rowCount = writeWb?.sheets?.get(0)?.rows
        closeXls()

        return rowCount
    }

    fun getSupCodeList(): MutableList<String>{
        val lstSupCode = ArrayList<String>()

        try {
            openXls()

            val wSheet: Sheet = wb!!.getSheet(0)

            val iRows = wSheet.rows

            // Skip 1 row >> Header.
            // Column 0 = Sup Code.
            for(row in 1 until iRows){
               val cellValue = wSheet.getCell(0,row).contents
                if(cellValue.isNotEmpty()){
                    if(!lstSupCode.contains(cellValue.trim()))
                        lstSupCode.add(cellValue.trim())
                }
            }
        }catch(e: Exception){
            Log.e(TAG, e.message.toString())
        }finally {
            closeXls()
        }

        return lstSupCode
    }

    fun getLocCodeList(): MutableList<String>{
        val lstLocCode = ArrayList<String>()

        try {
            openXls()

            val wSheet: Sheet = wb!!.getSheet(0)

            val iRows = wSheet.rows

            // Skip 1 row >> Header.
            // Column 1 = Loc Code.
            for(row in 1 until iRows){
                val cellValue = wSheet.getCell(1,row).contents
                if(cellValue.isNotEmpty()){
                    if(!lstLocCode.contains(cellValue.trim()))
                        lstLocCode.add(cellValue.trim())
                }
            }
        }catch(e: Exception){
            Log.e(TAG, e.message.toString())
        }finally {
            closeXls()
        }

        return lstLocCode
    }


    fun deleteRow(row2Delete: Int) : Boolean{
        try {
            openXls()

            val wSheet = writeWb?.getSheet(0)

            val iRows = wSheet?.rows
            if (iRows != null) {
                if(iRows > row2Delete){
                    wSheet.removeRow(row2Delete)
                }
            }
            return true
        }catch(e: Exception){
            Log.e(TAG, e.message.toString())
        }finally {
            closeXls()
        }

        return false
    }
}