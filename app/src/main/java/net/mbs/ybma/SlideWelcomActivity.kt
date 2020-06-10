package net.mbs.ybma

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_slide_welcom.*
import net.mbs.ybma.adapter.MySliderAdapter
import net.mbs.ybma.commons.PrefManager
import net.mbs.ybma.models.IntroSlide

class SlideWelcomActivity : AppCompatActivity() {
    private val sliderAdapter= MySliderAdapter(
        listOf(
            IntroSlide(
                "Réserver votre Taxi",
                "Créez un compte, choisissez un trajet, recevez une confirmation de votre trajet, faites votre trajet, payez et notez votre conducteur",
                R.drawable.intro_1

            ),
            IntroSlide(
                "Messagerie",
                "Communiquez avec n'importe quel conducteur en appelant ou en écrivant un message",
                R.drawable.intro_2

            )
        )
    )

    var prefManager: PrefManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_welcom)
        // Vérification du premier lancement - avant d'appeler setContentView()
        prefManager = PrefManager(this)

        if (!prefManager!!.isFirstTimeLaunch2()) {
            gotoLoginScreen()
            finish()
        }

        // notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        // making notification bar transparent
        changeStatusBarColor()

        //init slide welcom

        viewPager.adapter = sliderAdapter

        //passer sur le slide suivant
        btn_next.setOnClickListener {
            if(viewPager.currentItem +1 < sliderAdapter.itemCount ){
                viewPager.currentItem+=1
            }else{
                gotoLoginScreen()
            }
        }

        btn_skip.setOnClickListener {
            gotoLoginScreen()
        }


    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }


    private fun gotoLoginScreen() {
        prefManager!!.setFirstTimeLaunch2(false);
        //startActivity(Intent(this, LoginActivity::class.java));
        finish();
    }

    //Listener on Page Change
    var viewPagerPageChangeListener = object : ViewPager.OnPageChangeListener{

        override fun onPageScrollStateChanged(state: Int) {
            TODO("Not yet implemented")
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            TODO("Not yet implemented")
        }

        override fun onPageSelected(position: Int) {
            addBottomDots(position)
        }

    }

    private fun addBottomDots(position: Int) {

    }
}