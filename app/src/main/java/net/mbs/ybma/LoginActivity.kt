package net.mbs.ybma

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_login.*
import net.mbs.ybma.commons.PrefManager
import net.mbs.ybma.retrofit.IUserClients
import net.mbs.ybma.retrofit.RetrofitAppClient
import net.mbs.ybma.utils.CustomDialog
import java.util.concurrent.TimeUnit

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
    lateinit var userClients: IUserClients
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
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
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

        //init retrofit
        userClients= RetrofitAppClient.getInstance().create(IUserClients::class.java)

    }


    private fun initView() {

        resendOtp.visibility = View.GONE
        otp_layout.visibility = View.GONE

        //envoi du numero
        send_otp.setOnClickListener {
            progressBar_login.visibility = View.VISIBLE
            val numWithCode = "+" + ccp.selectedCountryCode + input_phone!!.text.toString()
            startPhoneNumberVerification(numWithCode!!)
            starTimer()
        }
        //Envoi la verification
        btn_valide.setOnClickListener(View.OnClickListener {
            verifyPhoneNumberWithCode(mverificationId, input_otp.text.toString().trim())
        })
    }

    private fun phoneNumberAuthCallbackListener() {
        mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential.smsCode)
                progressBar_login!!.visibility = View.GONE
                input_otp.setText(phoneAuthCredential.smsCode)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(
                        this@LoginActivity,
                        " " + e.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                } else if (e is FirebaseTooManyRequestsException) {
                    Toast.makeText(
                        this@LoginActivity,
                        " " + e.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                }
                Toast.makeText(this@LoginActivity, " " + e.message.toString(), Toast.LENGTH_SHORT)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d(TAG, "onCodeSent:" + verificationId)

                mverificationId = verificationId
                form_layout.visibility = View.GONE
                input_otp.visibility = View.VISIBLE
                otp_layout.visibility = View.VISIBLE
            }

        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    input_otp.setText("")
                    progressBar_login!!.visibility = View.VISIBLE
                    // checkUserfromFirebase(firebaseAuth.currentUser!!.phoneNumber)
                    AsyncTask.execute { login() }
                } else {
                    Log.w(TAG, "signInWithCredential:echoué", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(
                            this, "Échec de la vérification! Veuillez saisir le bon code",
                            Toast.LENGTH_SHORT
                        )
                    }
                }
            }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        progressBar_login!!.visibility = View.GONE
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            mCallBack
        ) // OnVerificationStateChangedCallbacks
    }

    private fun verifyPhoneNumberWithCode(verificationId: String, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun starTimer() {
        timer = object : CountDownTimer(6000, 1000) {
            override fun onFinish() {
                skip.visibility = View.GONE
                resendOtp.visibility = View.VISIBLE
            }

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                skip.visibility = View.VISIBLE
                skip.text = "Temps restant" + millisUntilFinished / 1000
            }

        }.start()
    }

    private fun login() {
        TODO("Not yet implemented")
    }
}