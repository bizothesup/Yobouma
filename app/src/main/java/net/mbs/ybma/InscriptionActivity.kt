package net.mbs.ybma

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_inscription.*
import net.mbs.ybma.commons.HelperCommon
import net.mbs.ybma.commons.HelperUrl
import net.mbs.ybma.commons.PrefManager
import net.mbs.ybma.models.User
import net.mbs.ybma.retrofit.IUserClients
import net.mbs.ybma.retrofit.RetrofitAppClient
import net.mbs.ybma.retrofit.response.UserResponse
import net.mbs.ybma.utils.CustomDialog
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InscriptionActivity : AppCompatActivity() {
    val TAG = "INSCMBS"
    lateinit var userClients: IUserClients
    private var compositeDisposable: CompositeDisposable? = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscription)
        val objetbundle = this.intent.extras
        val phoneParam: String? = objetbundle!!.getString("phone")
        phone_ins.setText(phoneParam)

        //init retrofit
        userClients = RetrofitAppClient.getInstance().create(IUserClients::class.java)

        send.setOnClickListener {
            val nom = nom_ins.text.toString()
            val prenom = prenom_ins.text.toString()
            val phone = phone_ins.text.toString()
            submitFormInscription(nom, prenom, phone)
        }
    }

    fun submitFormInscription(
        nom: String,
        prenom: String,
        phone: String
    ) {
        if (!validateNom()) {
            return
        }
        if (!validatePrenom()) {
            return
        }
        createUser(nom, prenom, phone)
    }

    private fun validateNom(): Boolean {
        if (nom_ins.text.toString().trim().isEmpty()) {
            input_layout_nom_ins.error = getResources().getString(R.string.enter_your_name)
            requestFocus(nom_ins)
            return false
        } else {
            input_layout_nom_ins.isErrorEnabled = false
        }
        return true
    }

    private fun validatePrenom(): Boolean {
        if (prenom_ins.text.toString().trim().isEmpty()) {
            input_layout_prenom_ins.error = getResources().getString(R.string.enter_your_firstname)
            requestFocus(prenom_ins)
            return false
        } else {
            input_layout_prenom_ins.isErrorEnabled = false
        }
        return true
    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    private fun createUser(nom: String, prenom: String, phone: String) {
        CustomDialog.progressDialog(this).show()
        var userResponse: UserResponse = UserResponse()
        //request Login
        val userLog = userClients.userRegister(key = HelperUrl.KEY,phone = phone,
            nom = nom,prenom = prenom, account_type = "client",login_type = "phone",tonotify = "yes")
        userLog.enqueue(object: Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d(TAG,t.message)
            }

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                userResponse = response.body()!!
                //Etat=1 successs save profile redirect Home
                //Etat=2 No found redirect to register save profile redirect Home
                //Etat=3 No active
                //Etat=0 Erre Request 500

                if (userResponse.etat!! == "1"){
                val user:User=userResponse.user!!
                    HelperCommon.saveProfile(user = user,context = this@InscriptionActivity)
                    launchHomeScreen()
                }
                else if (userResponse.etat!! == "2"){

                    Toast.makeText(this@InscriptionActivity, "Le Numero existe Déja", Toast.LENGTH_SHORT).show()
                    requestFocus(phone_ins)
                    input_layout_phone_ins.setError("Entre un  autre Numéro")
                }
                else{
                    Toast.makeText(this@InscriptionActivity,"Erreur Server",Toast.LENGTH_SHORT).show()
                }
            }

        })

    }

    override fun onResume() {
        super.onResume()
        CustomDialog.progressDialog(this).dismiss()

    }

    override fun onStop() {
        super.onStop()
        compositeDisposable!!.clear()
    }
    fun launchHomeScreen() {
        CustomDialog.progressDialog(this).dismiss()
        val intent = Intent(this@InscriptionActivity, HomeActivity::class.java)
        intent.putExtra("fragment_name", "")
        startActivity(intent)
    }
}