package net.mbs.ybma.commons

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_inscription.*
import net.mbs.ybma.models.User
import net.mbs.ybma.retrofit.IUserClients
import net.mbs.ybma.retrofit.RetrofitAppClient
import net.mbs.ybma.retrofit.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object HelperCommon {
    var fcm_id: String? = null

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }

    fun changeColor(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val win: Window = window
            win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            win.statusBarColor = Color.TRANSPARENT
        }
    }

    fun saveProfile(user: User, context: Context) {
        SessionUser.setNom(user.nom, context)
        SessionUser.setPrenom(user.prenom, context)
        SessionUser.setPhone(user.phone, context)
        SessionUser.setEmail(user.email, context)
        SessionUser.setID(user.id, context)
        SessionUser.setlogintype(user.login_type, context)
        SessionUser.setUserName(user.nom + " " + user.prenom, context)
        SessionUser.setUserCategorie(user.user_cat, context)
        SessionUser.setCurrency(user.country, context)
        SessionUser.setPhoto(user.phone, context)
        SessionUser.setCountry(user.country, context)

        if (user.tonotify.equals("yes")) {
            SessionUser.setPushNotification(true, context)
        } else {
            SessionUser.setPushNotification(false, context)
        }
        updateFCM(SessionUser.getID(context), context)
    }

    private fun updateFCM(id: String?, context: Context) {
        val fcmid = arrayOf("")
        val deviceid = arrayOf("")

        if (fcm_id == null) {
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@addOnCompleteListener
                    }
                    ////Getnew instance Token
                    val token = task.result!!.token
                    fcmid[0] = token
                    // Log and toast
                    deviceid[0] = Settings.Secure.getString(
                        context.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                    if (fcmid[0].trim { it <= ' ' }.isNotEmpty() && deviceid[0].trim { it <= ' ' }
                            .isNotEmpty()) {
                        setUserFCM(id, fcmid[0], deviceid[0])
                    }
                }
        }else{
            fcmid[0] =fcm_id!!
        }

    }

    private fun setUserFCM(id: String?, fcmid: String?, deviceid: String?) {
        var userResponse: UserResponse = UserResponse()
        val userClients = RetrofitAppClient.getInstance().create(IUserClients::class.java)
        val userLog = userClients.userUpdateFcm(key = HelperUrl.KEY,user_id = id!!,
            fcm_id = fcmid!!,user_cat = "client",device_id = deviceid!!)
        userLog.enqueue(object: Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("",t.message)
            }

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                userResponse = response.body()!!
                Log.d("Set UPDATE FCM", "Mbs Update FCM  ${response.body()!!.message}")
            }
        })
    }
}