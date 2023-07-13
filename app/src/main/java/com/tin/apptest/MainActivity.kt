package com.tin.apptest

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.tin.apptest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnTest1.setOnClickListener {
            loadTest1()
        }

        binding.btnTest2.setOnClickListener {
            loadTest2()
        }

        binding.btnTest3.setOnClickListener {
            loadTest3()
        }


        if(hasPermission(*permissions)){
            // Enable main controls.
        }else if (shouldShowRequestPermissionRationale(permissions[0])){
            // Show a message box and ask for permission.
            showPermissionDialog()
        }else{
            // Ask for permission.
            // Disable on screen main controls.

            // Request for permissions.
            ActivityCompat.requestPermissions(this,permissions, 101)
        }

    }

    private fun showPermissionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("The app needs to have Write External Storage Permission.\nPlease grant the permission on next screen!")
            .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                ActivityCompat.requestPermissions(this,permissions,101)
            })
        builder.create()
        builder.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            101 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Enable on screen main controls.
                }else{
                    // Explain to user on why we need the permission.
                }
                return
            }
        }
    }

    private fun hasPermission(vararg permissions: String) : Boolean {
        return permissions.all { ActivityCompat.checkSelfPermission(this,it) == PackageManager.PERMISSION_GRANTED }
    }

    private fun loadTest1(){
        val intent = Intent(this,Test1Activity::class.java)
        this.startActivity(intent)
    }

    private fun loadTest2(){

    }

    private fun loadTest3(){

    }
}