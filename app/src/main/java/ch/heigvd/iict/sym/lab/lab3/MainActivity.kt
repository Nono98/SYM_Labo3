package ch.heigvd.iict.sym.lab.lab3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ch.heigvd.iict.sym.lab.lab3.ibeacon.iBeaconActivity

class MainActivity : AppCompatActivity() {
    private lateinit var barcodeButton : Button
    private lateinit var nfcButton : Button
    private lateinit var ibeaconButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        barcodeButton = findViewById(R.id.barcode)
        nfcButton = findViewById(R.id.nfc)
        ibeaconButton = findViewById(R.id.ibeacon)

        barcodeButton.setOnClickListener {
            val intent = Intent(this, BarcodeActivity::class.java)
            startActivity(intent)
        }

        nfcButton.setOnClickListener {
            val intent = Intent(this, NFCActivity::class.java)
            startActivity(intent)
        }

        ibeaconButton.setOnClickListener {
            val intent = Intent(this, IBeaconActivity::class.java)
            startActivity(intent)
        }
    }
}