package ch.heigvd.iict.sym.lab.lab3

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.google.zxing.client.android.BeepManager

import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory

import com.google.zxing.BarcodeFormat
import java.util.*
import com.google.zxing.ResultPoint
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ch.heigvd.iict.sym.lab.lab3.R

import com.journeyapps.barcodescanner.BarcodeResult

import com.journeyapps.barcodescanner.BarcodeCallback

/**
 *
 */
class BarcodeActivity : AppCompatActivity() {

    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var beepManager: BeepManager
    private lateinit var imageView: ImageView
    private lateinit var lastText: TextView

    /**
     * À la création, initialisation de la vue, récupération des références.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode)

        barcodeView = findViewById(R.id.barcode_scanner)
        val formats: Collection<BarcodeFormat> =
            Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        barcodeView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView.initializeFromIntent(intent)
        barcodeView.decodeContinuous(callback)

        beepManager = BeepManager(this)

        imageView=findViewById(R.id.barcodePreview)
        lastText=findViewById(R.id.barcode_result)



        // Ask user for fine location permission
        if ((ContextCompat.checkSelfPermission(this@BarcodeActivity,
                Manifest.permission.CAMERA
            ) and PackageManager.PERMISSION_GRANTED) == 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@BarcodeActivity,
                    Manifest.permission.CAMERA
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@BarcodeActivity,
                    arrayOf(Manifest.permission.CAMERA), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@BarcodeActivity,
                    arrayOf(Manifest.permission.CAMERA), 1
                )
            }
        }
    }



    /**
     * Callback lorsqu'un barre code est décodé.
     */
    private var callback: BarcodeCallback? = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || result.text == lastText.text) {
                // Prevent duplicate scans
                return
            }
            lastText.text = result.text
            beepManager.playBeepSoundAndVibrate()

            //Added preview of scanned barcode
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW))
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    /**
     * À la récupération
     */
    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    /**
     * Mise en pause
     */
    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }

    /**
     * Sur pression
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }
}