package net.mbs.ybma.fragment.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import net.mbs.ybma.R

class HomeFragment : Fragment(), OnMapReadyCallback{


    private lateinit var homeViewModel: HomeViewModel

    //GoogleMap
    private lateinit var mMap: GoogleMap

    //View
    var mapView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        //Carte Map
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapView = mapFragment.view
        mapFragment.getMapAsync(this)

        return root
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        mMap.isMyLocationEnabled = true

        //Placer le button zoom position to bottom
        if (mapView != null && mapView!!.findViewById<View?>("1".toInt()) != null) {
            val locationButton = (mapView!!.findViewById<View>("1".toInt()).parent as View).findViewById<View>("2".toInt())
            val layoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            layoutParams.setMargins(0, 20, 20, 550)
            val zoomButton = (mapView!!.findViewById<View>("1".toInt()).parent as View).findViewById<View>("1".toInt())
            val layoutParamsZoom = zoomButton.layoutParams as RelativeLayout.LayoutParams
            layoutParamsZoom.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            layoutParamsZoom.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            layoutParamsZoom.setMargins(0, 20, 20, 200)
        }



//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        //mMap.setOnMyLocationButtonClickListener(this)
      // mMap.setOnMyLocationClickListener(this)
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL



    }


}

