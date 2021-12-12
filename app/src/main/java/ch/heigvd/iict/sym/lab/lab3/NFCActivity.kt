package ch.heigvd.iict.sym.lab.lab3

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import android.content.IntentFilter

import android.app.PendingIntent
import android.content.IntentFilter.MalformedMimeTypeException
import android.util.Log


class NFCActivity : AppCompatActivity() {

    val TAG = "NfcDemo"

    private var mTextView: TextView? = null
    private var mNfcAdapter: NfcAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfcactivity)

        mTextView = findViewById(R.id.textView_explanation);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show()
            finish();
            return;
        } else if(!mNfcAdapter!!.isEnabled()) {
            Toast.makeText(this, "NFC is disabled", Toast.LENGTH_LONG).show()
        }


        handleIntent(getIntent());
    }

    private fun handleIntent(intent: Intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
                // Process the messages array.
            }
        }
    }

    override fun onResume(){
        super.onResume()
        setupForegroundDispatch();
    }

    private fun setupForegroundDispatch() {
        if (mNfcAdapter == null) return
        val intent = Intent(this.applicationContext, this.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(this.applicationContext, 0, intent, 0)
        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()
        // On souhaite être notifié uniquement pour les TAG au format NDEF
        filters[0] = IntentFilter()
        filters[0]!!.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
        filters[0]!!.addCategory(Intent.CATEGORY_DEFAULT)
        try {
            filters[0]!!.addDataType("text/plain")
        } catch (e: MalformedMimeTypeException) {
            Log.e(TAG, "MalformedMimeTypeException", e)
        }
        mNfcAdapter!!.enableForegroundDispatch(this, pendingIntent, filters, techList)
    }

    override fun onPause(){
        super.onPause()
        stopForegroundDispatch();
    }
    private fun stopForegroundDispatch() {
        if (mNfcAdapter != null) mNfcAdapter!!.disableForegroundDispatch(this)
    }


}