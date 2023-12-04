package com.tin.apptest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.tin.apptest.databinding.ActivityTest3Binding

class Test3Activity : AppCompatActivity() {

    lateinit var binding: ActivityTest3Binding
    val TAG = "TEST3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTest3Binding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnClear.setOnClickListener {
            binding.etInput.setText("")
            binding.etOutput.text = null

            binding.etInput.requestFocus()
        }

        binding.btnRead.setOnClickListener {
            qrLogic()
        }
    }


    // QR reading logic.
    /*
        1. Product Code ==>  + E235 (PCode) 0 (1)
        2. Product Code + Lot No ==> + E235 (PCode) 0 / $$ 7 (LotNo) (1)
        3. Product Code + Expiry Date (4) + Lot No ==> + E235 (PCode) 0 / $$ MMYY (LotNo) (1)
        4. Product Code + QTY + Expiry Date (4) + Lot No ==> + E235 (PCode) 9 / $$ 9 (5) MMYY (LotNo) (1)
        5. Product Code + QTY + Lot No ==> + E235 (PCode) 9 / $$ 9 (5) 7 (LotNo) (1)
        6. Product Code + QTY ==> + E235 (PCode) 9 / $$ 9 (5) (1)
        7. Product Code + Expiry Date (6) + Lot No ==> + E235 (PCode) 0 / $$ 3 YYMMDD (LotNo) (1)
        8. Product Code + QTY + Expiry Date (6) + Lot No ==> + E235 (PCode) 9 / $$ 9 (5) 3 YYMMDD (LotNo) (1)
        9. Product COde + Lot No ==> + E235 (PCode) 0 / $ (LotNo) (1)
     */
    private fun qrLogic() {

        var pCode = ""
        var expDateRaw = ""
        var expDate = ""
        var lotNo = ""
        var qty = ""

        val readData = binding.etInput.text.toString().trim()
        var data = readData

        try {
            if(!readData.contains("/")){
                // Pattern 1 --> Product Code ==>  + E235 (PCode) 0 (1)
                data = readData.uppercase().replace("+E235","")
                pCode = data.substring(0,data.length - 2)

            }else if (readData.contains("0/$$7")){
                // Pattern 2 --> Product Code + Lot No ==> + E235 (PCode) 0 / $$ 7 (LotNo) (1)
                data = readData.uppercase().replace("+E235","").replace("0/$$7", "@")
                pCode = data.substring(0, data.indexOf("@"))

                data = data.replace(pCode, "").replace("@","")
                lotNo = data.substring(0, data.length - 1)

            }else if(readData.contains("9/$$9")){
                // Pattern 4 --> Product Code + QTY + Expiry Date (4) + Lot No ==> + E235 (PCode) 9 / $$ 9 (5) MMYY (LotNo) (1)

                data = readData.uppercase().replace("+E235","").replace("9/$$9", "@")
                pCode = data.substring(0, data.indexOf("@"))

                data = data.replace(pCode,"").replace("@","")
                qty = data.substring(0,5)

                data = data.replace(qty,"")

                // Pattern 5 --> Product Code + QTY + Lot No ==> + E235 (PCode) 9 / $$ 9 (5) 7 (LotNo) (1)
                if(data.startsWith("7")){
                    data = data.substring(1)
                    lotNo = data.substring(0, data.length - 1)
                }else{
                    if(data.length < 2){
                        // Pattern 6 --> No need to do anything.
                    }else{

                        if(data.startsWith("3")){
                            // Pattern 8 --> Exp --> 6 digit
                            data = data.substring(1)
                            expDateRaw = data.substring(0,6)
                            expDate = expDateRaw.substring(2,4) + expDateRaw.substring(0,2)
                            data = data.replace(expDateRaw,"")
                        }else{
                            // Continue Pattern 4.
                            expDate = data.substring(0,4)
                            data = data.replace(expDate,"")
                        }


                        lotNo = data.substring(0, data.length - 1)
                    }
                }
            }else if(readData.contains("0/$$3")){
                // Pattern 7 --> Product Code + Expiry Date (6) + Lot No ==> + E235 (PCode) 0 / $$ 3 YYMMDD (LotNo) (1)

                data = readData.uppercase().replace("+E235","").replace("0/$$3", "@")
                pCode = data.substring(0, data.indexOf("@"))

                data = data.replace(pCode,"").replace("@","")
                expDateRaw = data.substring(0,6)
                expDate = expDateRaw.substring(2,4) + expDateRaw.substring(0,2)

                data = data.replace(expDateRaw,"")
                lotNo = data.substring(0, data.length - 1)
            }else if(readData.contains("0/$$")){
                // Pattern 3 --> Product Code + Expiry Date (4) + Lot No ==> + E235 (PCode) 0 / $$ MMYY (LotNo) (1)
                data = readData.uppercase().replace("+E235","").replace("0/$$", "@")
                pCode = data.substring(0, data.indexOf("@"))

                data = data.replace(pCode,"").replace("@","")
                expDate = data.substring(0,4)

                data = data.replace(expDate,"")
                lotNo = data.substring(0, data.length - 1)

            }else if(readData.contains("0/$")){
                // Pattern 9 --> Product COde + Lot No ==> + E235 (PCode) 0 / $ (LotNo) (1)

                data = readData.uppercase().replace("+E235","").replace("0/$", "@")
                pCode = data.substring(0, data.indexOf("@"))

                data = data.replace(pCode,"").replace("@","")
                lotNo = data.substring(0, data.length - 1)
            }

            Log.d(TAG, "QR Read Data: $readData")
            Log.d(TAG,"SKU: $pCode.   Expiry Date: $expDate.  LotNo: $lotNo.   Qty: $qty.")
            binding.etOutput.setText("SKU: $pCode.  Expiry Date: $expDate.  LotNo: $lotNo.   Qty: $qty.")
        }catch(e: Exception){
            Toast.makeText(this,"QR Code Logic Error: ${e.message.toString()}", Toast.LENGTH_LONG).show()
        }

    }
}