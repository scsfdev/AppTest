package com.tin.apptest

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tin.apptest.databinding.ActivityTest1Binding


class Test1Activity : AppCompatActivity() {
    lateinit var binding : ActivityTest1Binding
    private var xlsApi = ExcelApi(this)

    lateinit var adapterSup: ArrayAdapter<String>
    lateinit var adapterLoc: ArrayAdapter<String>

    private var supCodeList = mutableListOf<String>()
    private var locCodeList = mutableListOf<String>()

    private var previousFocus = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTest1Binding.inflate(layoutInflater)
        setContentView(binding.root)


        //binding.etPartNo.inputType = InputType.TYPE_NULL
        binding.etPartNo.showSoftInputOnFocus = false

        binding.groupAll.visibility = View.GONE

        binding.etPartNo.setOnFocusChangeListener { view, focus ->
            Log.d(xlsApi.TAG, "Focus: $focus.  Cur Focus: $previousFocus")
            if(!focus){
                previousFocus = 1
            }
        }

        binding.etExpiryDate.setOnFocusChangeListener { view, focus ->
            Log.d(xlsApi.TAG, "Focus: $focus.  Cur Focus: $previousFocus")
            if(!focus){
                previousFocus = 2
            }
        }

        binding.etCountQty.setOnFocusChangeListener { view, focus ->
            Log.d(xlsApi.TAG, "Focus: $focus.  Cur Focus: $previousFocus")
            if(!focus){
                previousFocus = 3
            }
        }

        binding.btnLoadXls.setOnClickListener {
            val rowCount = xlsApi.getRowsCount()
            binding.tvRecord.text = " of " + (rowCount ?: 0).toString()

            binding.groupAll.visibility = View.VISIBLE
            binding.groupView.visibility = View.GONE
            binding.groupInput.visibility = View.VISIBLE

            // Load Data.
            populateSpinner()

            binding.etPartNo.requestFocus()
        }

        binding.btnNew.setOnClickListener {
            binding.groupView.visibility = View.GONE
            binding.groupInput.visibility = View.VISIBLE

            binding.spinLoc.setSelection(0)
            binding.spinSupCode.setSelection(0)

            binding.etPartNo.requestFocus()
        }

        binding.btnView.setOnClickListener {
            binding.groupView.visibility = View.VISIBLE
            binding.groupInput.visibility = View.GONE

            binding.spinLoc.setSelection(0)
            binding.spinSupCode.setSelection(0)

            binding.etPartNo.requestFocus()
        }

        binding.btnTest1.setOnClickListener {
            Log.d(xlsApi.TAG, "Previous Focus: $previousFocus")
            Handler(Looper.getMainLooper()).postDelayed({
                // Your Code
                forceShowKeyboard()
            }, 500)

        }

        binding.btnTest2.setOnClickListener {
            deleteRow()
        }

        adapterSup = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, supCodeList)
        adapterLoc = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, locCodeList)
        adapterSup.setNotifyOnChange(true)
        adapterLoc.setNotifyOnChange(true)

        binding.spinSupCode.adapter = adapterSup
        binding.spinLoc.adapter = adapterLoc
    }

    private fun deleteRow(){
        // TODO: Delete excel row.
        var result = xlsApi.deleteRow(3)
        if(result){
            Toast.makeText(this,"Row successfully deleted.",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"Row deletion failed.",Toast.LENGTH_SHORT).show()
        }
    }

    fun forceShowKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
     //   imm.hideSoftInputFromWindow(view.windowToken, 0)
      //  imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT)

        imm.toggleSoftInput(0,0)

//        Log.d(xlsApi.TAG, "Previous Focus: $previousFocus")
//        when(previousFocus){
//            1 -> {
//             //   binding.etPartNo.requestFocus()
//                imm.showSoftInput(binding.etPartNo, InputMethodManager.SHOW_IMPLICIT)
//            }
//            2 -> {
//            //    binding.etExpiryDate.requestFocus()
//                imm.showSoftInput(binding.etExpiryDate, InputMethodManager.SHOW_IMPLICIT)
//            }
//            3 -> {
//             //   binding.etCountQty.requestFocus()
//                imm.showSoftInput(binding.etCountQty,InputMethodManager.SHOW_IMPLICIT)
//            }
//        }


    }
    override fun onResume() {
        super.onResume()

        val rowCount = xlsApi.getRowsCount()
        binding.tvRecord.text = " of " + (rowCount ?: 0).toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        xlsApi.closeXls()
    }


    private fun populateSpinner(){
        supCodeList = xlsApi.getSupCodeList()

        locCodeList = xlsApi.getLocCodeList()

        adapterSup.clear()
        adapterSup.addAll(supCodeList)

        adapterLoc.clear()
        adapterLoc.addAll(locCodeList)
        adapterSup.notifyDataSetChanged()
        adapterLoc.notifyDataSetChanged()
    }


}