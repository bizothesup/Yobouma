package net.mbs.ybma

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import net.mbs.ybma.commons.HelperCommon
import net.mbs.ybma.commons.PrefManager

class SplashActivity : AppCompatActivity() {
    var prefManager: PrefManager?=null
    val SPLASH_SCREEN: Long = 5000
     var handler: Handler?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        //notification bar transparent
        HelperCommon.changeColor(window)
        prefManager= PrefManager(this)
        handler= Handler()
        checkInternet()


    }

    private fun checkInternet() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : MultiplePermissionsListener {
                @SuppressLint("ShowToast")
                @RequiresApi(Build.VERSION_CODES.M)
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (HelperCommon.isOnline(this@SplashActivity)) {
                        if(!prefManager!!.isFirstTimeLaunch()){
                            goToSlideWelcomeScreen()
                        }else{
                            timeHandler()
                        }
                    } else {
                        Toast.makeText(this@SplashActivity,"Veillez vous connecter sur Internet!!!!",
                            LENGTH_SHORT
                        )
                        handler!!.postDelayed({
                            finish()
                        }, SPLASH_SCREEN)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    AlertDialog.Builder(this@SplashActivity)
                        .setTitle(R.string.internet_permission_title)
                        .setMessage(R.string.internet_permission_message)
                        .setNegativeButton(
                            android.R.string.cancel
                        ) { dialog, which ->
                            dialog.dismiss();
                            p1?.cancelPermissionRequest()
                        }
                        .setPositiveButton(
                            android.R.string.ok
                        ) { dialog, which ->
                            dialog.dismiss()
                            p1?.continuePermissionRequest()
                        }.show()
                }

            } ).check()

    }

    private fun timeHandler() {
        handler!!.postDelayed({
            goToSlideWelcomeScreen()
        }, SPLASH_SCREEN)
    }

    private fun goToSlideWelcomeScreen() {
        startActivity(Intent(this, SlideWelcomActivity::class.java))
        finish()
    }

}