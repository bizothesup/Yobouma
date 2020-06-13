package net.mbs.ybma.fragment.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.android.synthetic.main.fragment_home.*
import net.mbs.ybma.R
import net.mbs.ybma.commons.SessionUser
import java.util.*

class HomeFragment : Fragment(), OnMapReadyCallback{



    private lateinit var homeViewModel: HomeViewModel

    //GoogleMap
    private lateinit var mMap: GoogleMap
    private var places: PlacesClient?=null
    //View
    var mapView: View? = null
    private var input_text_depart: EditText?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        //Init Place for Autocomplet
        val key_Gmap = resources.getString(R.string.google_maps_key)
        if(!Places.isInitialized()){
            Places.initialize(requireActivity().applicationContext,key_Gmap)
        }
        places= Places.createClient(requireContext())

        //Init Autocomplet Fragment Depart
        val autocomplete_fragment_depart = childFragmentManager.findFragmentById(R.id.autocomplete_fragment_depart) as AutocompleteSupportFragment


        //set Icon
        val  search_icon_depart = (autocomplete_fragment_depart.view as LinearLayout).getChildAt(0) as ImageView
        input_text_depart= (autocomplete_fragment_depart.view as LinearLayout).findViewById<EditText>(R.id.places_autocomplete_search_input)
        input_text_depart!!.textSize=16.0f
        input_text_depart!!.hint=resources.getString(R.string.depart)

        //Filitre Pays
        if(SessionUser.getCountry(requireContext())!! != "All")
            autocomplete_fragment_depart.setCountry(SessionUser.getCountry(requireContext()))

        autocomplete_fragment_depart.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.NAME))

        ///Listerner des Places write to interfaces
        autocomplete_fragment_depart.setOnPlaceSelectedListener(object: PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Log.i("Home PlaceListener ", "Place: " + place.name + ", " + place.id);
            }

            override fun onError(status: Status) {
                Log.d("Home Error",status.statusMessage)
            }

        })



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

