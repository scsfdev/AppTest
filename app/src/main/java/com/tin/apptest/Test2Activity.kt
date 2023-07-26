package com.tin.apptest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tin.apptest.databinding.ActivityTest2Binding
import java.nio.charset.StandardCharsets
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


class Test2Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTest2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnClear.setOnClickListener {
            binding.etInput.setText("")
            binding.etOutput.text = null

            binding.etInput.requestFocus()
        }

        binding.btnEncode.setOnClickListener {
            encodeData()
        }

        binding.btnDecode.setOnClickListener {
            deCodeData()
        }
    }

    private fun encodeData(){
        var data = binding.etOutput.text.toString()
        data = data + "\n\r" + binding.etInput.text.toString().encode64()
        binding.etOutput.setText(data)
    }

    private fun deCodeData(){
        var data = binding.etOutput.text.toString()
        data = data + "\n\r" + binding.etInput.text.toString().decode64()
        binding.etOutput.setText(data)
    }


    @OptIn(ExperimentalEncodingApi::class)
    private fun String.encode64(): String{
        return Base64.encode(this.toByteArray(StandardCharsets.UTF_8))
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun String.decode64(): String{
        return Base64.decode(this).toString(StandardCharsets.UTF_8)
    }
}