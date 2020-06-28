package net.mbs.ybma

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.navigation.NavigationView
import net.mbs.ybma.assync.TaskLoadedCallback
import net.mbs.ybma.commons.PrefManager
import net.mbs.ybma.commons.SessionUser
import net.mbs.ybma.fragment.ui.home.HomeFragment
import net.mbs.ybma.utils.Tools

class HomeActivity : AppCompatActivity(), TaskLoadedCallback {

    companion object {
        var context: Context? = null
        var activity: Activity? = null
        var prefManager: PrefManager? = null
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var actionBar: ActionBar? = null
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        context = this@HomeActivity
        activity = this

        prefManager = PrefManager(this)

        initToolbar()
        initNavigationMenu()

    }

    private fun initToolbar() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setHomeButtonEnabled(true)
        actionBar!!.title = "Admin"
        Tools.setSystemBarColor(this, R.color.colorPrimary)
    }

    private fun initNavigationMenu() {
        val nav_view = findViewById<View>(R.id.nav_view) as NavigationView
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        // open drawer at start
        drawer.openDrawer(GravityCompat.START)
    }

    //Chose fragement
    fun selectItem(post: Int) {
        var fragment: Fragment? = null
        //val pos =
        //var item:String

    }


    override fun onTaskDone(vararg values: Any) {
        if (SessionUser.getUserCategorie(context!!).equals("client")
        ) {
            if (SessionUser.getCurrentFragment(context!!).equals("home")) {
                if (HomeFragment.currentPolyline != null) HomeFragment.currentPolyline!!.remove()
                HomeFragment.currentPolyline =
                    HomeFragment.mMap.addPolyline(values[0] as PolylineOptions?)
                HomeFragment.currentPolyline!!.color = Color.DKGRAY
                val builder = LatLngBounds.Builder()
                val latLng1 = LatLng(
                    HomeFragment.departLocationReservation.latitude,
                    HomeFragment.departLocationReservation.longitude
                )
                val latLng2 = LatLng(
                    HomeFragment.destinationLocationReservation.latitude,
                    HomeFragment.destinationLocationReservation.longitude
                )
                builder.include(latLng1)
                builder.include(latLng2)
                HomeFragment.mMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        builder.build(),
                        17
                    )
                )
            } /* else if (M.getCurrentFragment(context).equals("mes_requetes_accueil")){
                if (FragmentHome.currentPolyline != null)
                    FragmentHome.currentPolyline.remove();
                FragmentHome.currentPolyline = FragmentHome.mMap.addPolyline((PolylineOptions) values[0]);
                FragmentHome.currentPolyline.setVehicleColor(Color.DKGRAY);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                LatLng latLng1 = new LatLng(FragmentHome.departLocationMesRequetes.getLatitude(), FragmentHome.departLocationMesRequetes.getLongitude());
                LatLng latLng2 = new LatLng(FragmentHome.destinationLocationMesRequetes.getLatitude(), FragmentHome.destinationLocationMesRequetes.getLongitude());
                builder.include(latLng1);
                builder.include(latLng2);
                FragmentHome.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 17));
            } else if (M.getCurrentFragment(context).equals("historic")){
                if (BottomSheetFragmentHistoric.currentPolyline != null)
                    BottomSheetFragmentHistoric.currentPolyline.remove();
                BottomSheetFragmentHistoric.currentPolyline = BottomSheetFragmentHistoric.mMap.addPolyline((PolylineOptions) values[0]);
                BottomSheetFragmentHistoric.currentPolyline.setVehicleColor(Color.DKGRAY);

                LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
                LatLng latLng3 = new LatLng(BottomSheetFragmentHistoric.clientLocation.getLatitude(),BottomSheetFragmentHistoric.clientLocation.getLongitude());
                LatLng latLng4 = new LatLng(BottomSheetFragmentHistoric.destinationLocation.getLatitude(),BottomSheetFragmentHistoric.destinationLocation.getLongitude());
                builder2.include(latLng3);
                builder2.include(latLng4);
//            BottomSheetFragmentRide.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder2.build(), 100));
            }else{
                if (BottomSheetFragmentMyRide.currentPolyline != null)
                    BottomSheetFragmentMyRide.currentPolyline.remove();
                BottomSheetFragmentMyRide.currentPolyline = BottomSheetFragmentMyRide.mMap.addPolyline((PolylineOptions) values[0]);
                BottomSheetFragmentMyRide.currentPolyline.setVehicleColor(Color.DKGRAY);

                LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
                LatLng latLng3 = new LatLng(BottomSheetFragmentMyRide.clientLocation.getLatitude(),BottomSheetFragmentMyRide.clientLocation.getLongitude());
                LatLng latLng4 = new LatLng(BottomSheetFragmentMyRide.destinationLocation.getLatitude(),BottomSheetFragmentMyRide.destinationLocation.getLongitude());
                builder2.include(latLng3);
                builder2.include(latLng4);
                BottomSheetFragmentMyRide.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder2.build(), 100));
            }*/
        } else {
            /*if(M.getCurrentFragment(context).equals("requete")){
                if (BottomSheetFragmentRide.currentPolyline != null)
                    BottomSheetFragmentRide.currentPolyline.remove();
                BottomSheetFragmentRide.currentPolyline = BottomSheetFragmentRide.mMap.addPolyline((PolylineOptions) values[0]);
                BottomSheetFragmentRide.currentPolyline.setVehicleColor(Color.DKGRAY);

                LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
                LatLng latLng3 = new LatLng(BottomSheetFragmentRide.clientLocation.getLatitude(),BottomSheetFragmentRide.clientLocation.getLongitude());
                LatLng latLng4 = new LatLng(BottomSheetFragmentRide.destinationLocation.getLatitude(),BottomSheetFragmentRide.destinationLocation.getLongitude());
                builder2.include(latLng3);
                builder2.include(latLng4);
//                BottomSheetFragmentRide.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder2.build(), 17));
            }else{
                if (BottomSheetFragmentCourse.currentPolyline != null)
                    BottomSheetFragmentCourse.currentPolyline.remove();
                BottomSheetFragmentCourse.currentPolyline = BottomSheetFragmentCourse.mMap.addPolyline((PolylineOptions) values[0]);
                BottomSheetFragmentCourse.currentPolyline.setVehicleColor(Color.DKGRAY);

                LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
                LatLng latLng3 = new LatLng(BottomSheetFragmentCourse.clientLocation.getLatitude(),BottomSheetFragmentCourse.clientLocation.getLongitude());
                LatLng latLng4 = new LatLng(BottomSheetFragmentCourse.destinationLocation.getLatitude(),BottomSheetFragmentCourse.destinationLocation.getLongitude());
                builder2.include(latLng3);
                builder2.include(latLng4);
//                BottomSheetFragmentCourse.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder2.build(), 17));
            }*/
        }
    }


}