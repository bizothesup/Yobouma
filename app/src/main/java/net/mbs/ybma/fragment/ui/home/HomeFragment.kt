package net.mbs.ybma.fragment.ui.home

import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import net.mbs.ybma.R
import net.mbs.ybma.commons.SessionUser
import java.util.*

class HomeFragment : Fragment(), OnMapReadyCallback{



    private lateinit var homeViewModel: HomeViewModel

    //GoogleMap
    private lateinit var mMap: GoogleMap
    private var places: PlacesClient?=null

    //Location
    var currentLocation: Location? = null
    var destinationLocation = Location("dummyprovider1")
    var departLocationReservation = Location("dummyprovider2")
    var destinationLocationReservation = Location("dummyprovider3")
    var departLocationMesRequetes = Location("dummyprovider2")
    var destinationLocationMesRequetes = Location("dummyprovider3")

    //Marker depart
    private var departMarkerReservation:Marker?=null
    private var departMarkerMesReservations:Marker?=null

    var tabLocation = ArrayList<Location>()

    //View
    var mapView: View? = null
    private var input_text_depart: EditText?=null
    private var PLACE_PICKER_REQUEST_RESERVATION_DEPART = 101


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)


        destinationLocation.latitude = 12.36858
        destinationLocation.longitude = -1.52709
        departLocationReservation.latitude = 12.36858
        departLocationReservation.longitude = -1.52709
        destinationLocationReservation.latitude = 12.36858
        destinationLocationReservation.longitude = -1.52709



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

        autocomplete_fragment_depart.setPlaceFields(
            mutableListOf(Place.Field.ID,Place.Field.NAME,
            Place.Field.LAT_LNG,Place.Field.ADDRESS,Place.Field.UTC_OFFSET)
        )

        ///Listerner des Places write to interfaces
        autocomplete_fragment_depart.setOnPlaceSelectedListener(object: PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Log.i("Home PlaceListener ", "Place: " + place.name + ", " + place.latLng)
                //recuperation localisation depart
                val latLng = place.latLng
                if(place.name!!.trim().isNotEmpty())
                    input_text_depart!!.setText(place.name)

                //ajouter Marker depart
                if (departLocationReservation != null && destinationLocationReservation != null) {

                   // departLocationReservation.latitude = latLng!!.latitude
             //       departLocationReservation.longitude = latLng.longitude
                    //tabLocation.add(departLocationReservation)


                    if(departMarkerReservation!=null)
                        departMarkerReservation!!.remove()

                    addMarkerDepar(LatLng(latLng!!.latitude,latLng.longitude))

                    //Positionnement camera
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13F))

                    val cameraPosition= CameraPosition.Builder()
                        .target(latLng)
                        .zoom(15F)
                        .bearing(90F)
                        .tilt(40F)
                        .build()
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))



                }


                Log.i("Home PlaceListener ", "Place: " + place.name + ", " + place.id)
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



    private fun addMarkerDepar(latLng: LatLng) {
        //Ajouter Marker
        val markerOptions = MarkerOptions()
        markerOptions.title(resources.getString(R.string.depart))
        markerOptions.snippet(resources.getString(R.string.point_depart))
        markerOptions.position(latLng)
        markerOptions.icon(generateBitmapDescriptorFromRes(requireContext(), R.drawable.ic_pin_2))
        departMarkerReservation = mMap.addMarker(markerOptions)
        departMarkerReservation!!.tag = resources.getString(R.string.depart)

    }

    private fun generateBitmapDescriptorFromRes(context: Context, resId: Int): BitmapDescriptor? {
        val drawable = ContextCompat.getDrawable(context, resId)
        drawable!!.setBounds(
            0,
            0,
            drawable.intrinsicWidth,
            drawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}

