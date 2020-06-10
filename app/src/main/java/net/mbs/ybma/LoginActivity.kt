package net.mbs.ybma

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import net.mbs.ybma.commons.PrefManager
import net.mbs.ybma.utils.CustomDialog

class LoginActivity : AppCompatActivity() {
    val TAG = "LOGINACTIVITY"
    private val APP_REQUEST_CODE = 7171 // Any number
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var listener: FirebaseAuth.AuthStateListener
    private lateinit var dialog: AlertDialog
    private lateinit var currentUser: FirebaseUser
    private lateinit var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mverificationId: String
    private lateinit var timer: CountDownTimer

    var prefManager: PrefManager? = null


    override fun onResume() {
        super.onResume()
        prefManager = PrefManager(this)
        if (!prefManager!!.isFirstTimeLaunch7()) {
            finish()
        }
        CustomDialog.progressDialog(this).dismiss()

    }


    fun launchHomeScreen() {
        CustomDialog.progressDialog(this).dismiss()
        prefManager!!.setFirstTimeLaunch7(false)
        //  val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.putExtra("fragment_name", "")
        startActivity(intent)
        finish()
    }

    fun launchResgisterScreen() {
        CustomDialog.progressDialog(this).dismiss()
        // val intent = Intent(this@LoginActivity, InscriptionActivity::class.java)
        // intent.putExtra("phone", nowithCode)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefManager = PrefManager(this)
        if (!prefManager!!.isFirstTimeLaunch7()) {
            launchHomeScreen();
        }
        initView()
        firebaseAuth = FirebaseAuth.getInstance()
        phoneNumberAuthCallbackListener()
    }

    private fun phoneNumberAuthCallbackListener() {
        TODO("Not yet implemented")
    }

    private fun initView() {
resendOtp.visibility=View.GONE
otp_layout.visibility =View.GONE

        //envoi du numero
        send_otp.setOnClickListener{
progressBar_login.visibility=View.VISIBLE
val numWithCode ="+" + ccp.selectedCountryCode + input_phone!!.text.toString()
            startPhoneNumberVerification(nowithCode!!)
            starTimer()
        }
    }
}