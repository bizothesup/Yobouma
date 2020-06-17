package net.mbs.ybma

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.navigation.NavigationView
import net.mbs.ybma.assync.TaskLoadedCallback
import net.mbs.ybma.commons.PrefManager
import net.mbs.ybma.commons.SessionUser
import net.mbs.ybma.fragment.ui.home.HomeFragment

class HomeActivity : AppCompatActivity() ,TaskLoadedCallback{

 companion object{
     private var context: Context? = null
     var activity: Activity? = null
     var prefManager: PrefManager? = null
 }
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        context = this@HomeActivity
        activity = this

        prefManager = PrefManager(this)



        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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