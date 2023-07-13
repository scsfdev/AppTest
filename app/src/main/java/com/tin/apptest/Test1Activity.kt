package com.tin.apptest

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.tin.apptest.databinding.ActivityTest1Binding


class Test1Activity : AppCompatActivity() {
    lateinit var binding : ActivityTest1Binding
    private var xlsApi = ExcelApi(this)

    lateinit var adapterSup: ArrayAdapter<String>
    lateinit var adapterLoc: ArrayAdapter<String>

    private var supCodeList = mutableListOf<String>()
    private var locCodeList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTest1Binding.inflate(layoutInflater)
        setContentView(binding.root)


        //binding.etPartNo.inputType = InputType.TYPE_NULL
        binding.etPartNo.showSoftInputOnFocus = false

        binding.groupAll.visibility = View.GONE

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

        binding.btnTest.setOnClickListener {
                forceShowKeyboard(it)
        }


        adapterSup = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, supCodeList)
        adapterLoc = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, locCodeList)
        adapterSup.setNotifyOnChange(true)
        adapterLoc.setNotifyOnChange(true)

        binding.spinSupCode.adapter = adapterSup
        binding.spinLoc.adapter = adapterLoc
    }

    fun forceShowKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
     //   inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        inputMethodManager.showSoftInput(view,0)
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