package net.mbs.ybma.commons

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import net.mbs.ybma.models.User

object HelperCommon {


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

    fun saveProfile(user: User,context: Context) {
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
    }
}