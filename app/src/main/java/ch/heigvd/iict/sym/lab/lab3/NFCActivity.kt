/*
Auteurs : Peguiron Adrien, Placherel Noémie, Viotti Nicolas

Cette activité permet à un utilisateur d'accèder à différents boutons après une authentification.
L'authentification se fait en deux parties, il se connecte d'abord à l'aide de credentials, et il doit
ensuite scanner une puce NFC pour prouver son identité.

3 niveaux d'authentification sont implémentés. Le but étant que l'utilisateur perde des privilièges si
il reste authentifié trop longtemps. Pour les regagner, il suffit de scanner à nouveau la puce NFC.
En cas de vol de téléphone, les données confidentielles ne pourraient donc pas être accédées par le voleur
(sauf dans le cas ou il aurait également volé la puce NFC)
 */

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

    // indique si l'utilisateur s'est loggé avec la paire username/password
    private var authenticated = false

    val TAG = "NfcDemo"

    // Les différents niveau d'authentification
    private final val AUTHENTICATE_MAX = 15
    private final val AUTHENTICATE_MEDIUM = 10
    private final val AUTHENTICATE_LOW = 5
    // Le niveau d'authentification actuel
    private var authenticateCurrent = 0

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
            // Si le NFC n'est pas supporté, on s'arrête
            Toast.makeText(this, "Cet appareil ne supporte pas le NFC", Toast.LENGTH_LONG).show()
            finish();
            return;
        } else if(!mNfcAdapter!!.isEnabled()) {
            Toast.makeText(this, "le NFC est désactivé", Toast.LENGTH_LONG).show()
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
            // Récupération du premier message NFC
            val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val ndefMsg = rawMessages?.get(0) as NdefMessage
            val ndefRecord = ndefMsg.records[0]
            if (String(ndefRecord.payload).contains("test")) {
                // Si le premier messaeg contient "test", le niveau d'authentification est alors au maximum
                authenticateCurrent = AUTHENTICATE_MAX + 1
                //source : https://stackoverflow.com/questions/10032003/how-to-make-a-countdown-timer-in-android
                // On perd 1 niveau d'authentification toute les 10 secondes
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
            Toast.makeText(this, "Login réussi", Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(this, "Echec du login", Toast.LENGTH_LONG).show()
        }
    }

    private fun nfcLogin(authenticationLevel : Int): Boolean {
        if ((authenticateCurrent >= authenticationLevel) && authenticated){
            Toast.makeText(this, "Accès accordé", Toast.LENGTH_LONG).show()
            return true
        }
        Toast.makeText(this, "Accès refusé", Toast.LENGTH_LONG).show()

        return false
    }

}