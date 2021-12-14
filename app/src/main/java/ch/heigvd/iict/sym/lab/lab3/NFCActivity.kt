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
import android.os.CountDownTimer
import android.widget.Button


class NFCActivity : AppCompatActivity() {

    private lateinit var loginButton : Button
    private lateinit var highButton : Button
    private lateinit var mediumButton : Button
    private lateinit var lowButton : Button
    private lateinit var username: TextView
    private lateinit var password: TextView

    private var authenticated = false
    val TAG = "NfcDemo"
    private final val AUTHENTICATE_MAX = 15
    private final val AUTHENTICATE_MEDIUM = 10
    private final val AUTHENTICATE_LOW = 5
    private var authenticateCurrent = 0

    private var mTextView: TextView? = null
    private var mNfcAdapter: NfcAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfcactivity)

        loginButton = findViewById(R.id.nfc_btnLogin)
        highButton = findViewById(R.id.nfc_btnHigh)
        mediumButton = findViewById(R.id.nfc_btnMid)
        lowButton = findViewById(R.id.nfc_btnLow)
        username = findViewById(R.id.nfc_username)
        password = findViewById(R.id.nfc_password)

        loginButton.setOnClickListener{
            login(username.text.toString(), password.text.toString())
        }

        highButton.setOnClickListener {
            nfcLogin(AUTHENTICATE_MAX)
        }

        mediumButton.setOnClickListener {
            nfcLogin(AUTHENTICATE_MEDIUM)
        }

        lowButton.setOnClickListener {
            nfcLogin(AUTHENTICATE_LOW)
        }

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show()
            finish();
            return;
        } else if(!mNfcAdapter!!.isEnabled()) {
            Toast.makeText(this, "NFC is disabled", Toast.LENGTH_LONG).show()
        }


        handleIntent(intent);
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        //source : https://www.andreasjakl.com/nfc-tags-ndef-and-android-with-kotlin/
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val ndefMsg = rawMessages?.get(0) as NdefMessage
            val ndefRecord = ndefMsg.records[0]
            if (String(ndefRecord.payload).contains("test")) {
                authenticateCurrent = AUTHENTICATE_MAX + 1
                //source : https://stackoverflow.com/questions/10032003/how-to-make-a-countdown-timer-in-android
                //on perd 1 niveau d'authentification toute les 10 secondes
                object : CountDownTimer((AUTHENTICATE_MAX * 10000).toLong(), 10000) {
                    override fun onTick(millisUntilFinished: Long) {
                        authenticateCurrent--
                    }

                    override fun onFinish() {
                        authenticateCurrent = 0
                    }
                }.start()
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
        stopForegroundDispatch()
    }

    private fun stopForegroundDispatch() {
        if (mNfcAdapter != null) mNfcAdapter!!.disableForegroundDispatch(this)
    }

    private fun login(username: String, password: String){
        if (username=="user1" && password=="mdpUser1!"){
            authenticated = true
            Toast.makeText(this, "login reussi", Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(this, "echec du login", Toast.LENGTH_LONG).show()
        }
    }

    private fun nfcLogin(authenticationLevel : Int): Boolean {
        if ((authenticateCurrent >= authenticationLevel) && authenticated){
            Toast.makeText(this, "acces accorde", Toast.LENGTH_LONG).show()
            return true
        }
        Toast.makeText(this, "acces refuse", Toast.LENGTH_LONG).show()

        return false
    }

}