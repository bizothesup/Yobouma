package net.mbs.ybma.fragment.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.LOCATION_SERVICE
import android.app.Activity.RESULT_CANCELED
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
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
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sucho.placepicker.AddressData
import com.sucho.placepicker.Constants
import com.sucho.placepicker.MapType
import com.sucho.placepicker.PlacePicker
import net.mbs.ybma.R
import net.mbs.ybma.commons.SessionUser
import java.io.IOException
import java.util.*

class HomeFragment : Fragment(), OnMapReadyCallback{



    private lateinit var homeViewModel: HomeViewModel

    //GoogleMap
    private lateinit var mMap: GoogleMap
    private var places: PlacesClient? = null

    //Location
    var currentLocation: Location? = null
    var destinationLocation = Location("dummyprovider1")
    var departLocationReservation = Location("dummyprovider2")
    var destinationLocationReservation = Location("dummyprovider3")
    var departLocationMesRequetes = Location("dummyprovider2")
    var destinationLocationMesRequetes = Location("dummyprovider3")
    var tabLocation = ArrayList<Location>()


    //Marker depart

    var departMarkerMesReservations: Marker? = null
    var currentMarker: Marker? = null
    var destinationMarker:Marker? = null

    var departMarkerReservation: Marker? = null
    var destinationMarkerReservation:Marker? = null

    var departMarkerMesRequetes: Marker? = null
    var destinationMarkerMesRequetes:Marker? = null



    //View
    var mapView: View? = null
    var input_text_depart: EditText? = null
    var input_text_arrivee: EditText? = null
    var my_location: ImageView? = null
    var chose_my_location: ImageView?=null
    var choose_my_location_destination:ImageView?=null

    //Google Api Client
    private lateinit var fusedLocationProviderClient:FusedLocationProviderClient
    private lateinit var locationRequest:LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var locationUpdateState = false

    companion object{
        private var PLACE_PICKER_REQUEST_RESERVATION_DEPART = 101
        private var PLACE_PICKER_REQUEST_RESERVATION_DESTINATION = 102
        private const val FAST_INTERVAL: Long=5000
        private const val UPDATE_INTERVAL: Long=5000
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }


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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        //Element view Init
        my_location = root.findViewById(R.id.my_location) as ImageView
        chose_my_location = root.findViewById(R.id.chose_my_location) as ImageView
        choose_my_location_destination = root.findViewById(R.id.choose_my_location_destination) as ImageView


        //Init Coordornnée
        destinationLocation.latitude = 12.647305
        destinationLocation.longitude = -7.943012
        departLocationReservation.latitude = 12.647305
        departLocationReservation.longitude = -7.943012
        destinationLocationReservation.latitude = 12.647305
        destinationLocationReservation.longitude = -7.943012

        //Location Request and CallBack
        createLocationRequest()
        startLocationOrUpdateCallBack()


        //Init Place for Autocomplet
        val key_Gmap = resources.getString(R.string.google_maps_key)
        if (!Places.isInitialized()) {
            Places.initialize(requireActivity().applicationContext, key_Gmap)
        }
        places = Places.createClient(requireContext())

        //Init Autocomplet Fragment Depart
        val autocomplete_fragment_depart =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment_depart) as AutocompleteSupportFragment

        //set Icon
        val search_icon_depart =
            (autocomplete_fragment_depart.view as LinearLayout).getChildAt(0) as ImageView
        search_icon_depart.setImageDrawable(resources.getDrawable(R.drawable.ic_navigator_depart))

        input_text_depart = autocomplete_fragment_depart.requireView()
            .findViewById(R.id.places_autocomplete_search_input)

        input_text_depart!!.textSize = 16.0f
        input_text_depart!!.hint = resources.getString(R.string.depart)

        //Filitre Pays
        if (SessionUser.getCountry(requireContext())!! != "All")
            autocomplete_fragment_depart.setCountry(SessionUser.getCountry(requireContext()))

        autocomplete_fragment_depart.setPlaceFields(
            mutableListOf(
                Place.Field.ID, Place.Field.NAME,
                Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.UTC_OFFSET
            )
        )

        ///Listerner des Places write to interfaces
        autocomplete_fragment_depart.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Log.i("Home PlaceListener ", "Place: " + place.name + ", " + place.latLng)
                if (departMarkerMesRequetes != null && destinationMarkerMesRequetes != null) {
                    departMarkerMesRequetes!!.remove()
                    destinationMarkerMesRequetes!!.remove()
                   // currentPolyline.remove()
                }

                //recuperation localisation depart
                val latLng = place.latLng
                if (place.name!!.trim().isNotEmpty())
                    input_text_depart!!.setText(place.name)

                //ajouter Marker depart
                if(destinationMarker != null)
                    destinationMarker!!.remove()
                if((departLocationReservation != null && destinationLocationReservation != null) && tabLocation.size > 1) {
//                    departMarkerReservation.remove()
//                    destinationMarkerReservation.remove()
                    tabLocation.clear()
                    if(departMarkerReservation != null)
                        departMarkerReservation!!.remove()
                    if(destinationMarkerReservation != null)
                        destinationMarkerReservation!!.remove()
                    //f(currentPolyline != null)
                      //  currentPolyline.remove();
                }

                if(departLocationReservation != null && destinationLocationReservation != null){
                    departLocationReservation.latitude = latLng!!.latitude
                    departLocationReservation.longitude = latLng.longitude
                    tabLocation.add(departLocationReservation)
                    if(departMarkerReservation != null)
                        departMarkerReservation!!.remove()
                    addMarkerDepar(LatLng(latLng.latitude, latLng.longitude))

                    //Positionnement camera
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13F))

                    val cameraPosition = CameraPosition.Builder()
                        .target(latLng)
                        .zoom(15F)
                        //.bearing(90F)
                       // .tilt(40F)
                        .build()
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))


                }


                Log.i("Home PlaceListener ", "Place: " + place.name + ", " + place.id)
            }

            override fun onError(status: Status) {
                Log.d("Home Error", status.statusMessage)
            }

        })

        //Init AutoComplete Fragment Arrivé
        val autocomplete_fragment_arrive =  childFragmentManager.findFragmentById(R.id.autocomplete_fragment_arrivee) as AutocompleteSupportFragment

        autocomplete_fragment_arrive.setPlaceFields(
            mutableListOf(
                Place.Field.ID, Place.Field.NAME,
                Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.UTC_OFFSET
            )
        )

        val search_icon_arrive =
            (autocomplete_fragment_arrive.view as LinearLayout).getChildAt(0) as ImageView
        search_icon_arrive.setImageDrawable(resources.getDrawable(R.drawable.ic_navigator))

        input_text_arrivee = autocomplete_fragment_arrive.requireView()
            .findViewById(R.id.places_autocomplete_search_input)

        input_text_arrivee!!.textSize = 16.0f
        input_text_arrivee!!.hint = resources.getString(R.string.arrivee)

        //Filitre Pays
        if (SessionUser.getCountry(requireContext())!! != "All")
            autocomplete_fragment_arrive.setCountry(SessionUser.getCountry(requireContext()))

        ///Listerner des Places write to interfaces
        autocomplete_fragment_arrive.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {

                if (departMarkerMesRequetes != null && destinationMarkerMesRequetes != null) {
                    departMarkerMesRequetes!!.remove()
                    destinationMarkerMesRequetes!!.remove()
                    // currentPolyline.remove()
                }

                //recuperation localisation depart
                val latLng = place.latLng
                if (place.name!!.trim().isNotEmpty())
                    input_text_arrivee!!.setText(place.name)

                //ajouter Marker depart
                if(destinationMarker != null)
                    destinationMarker!!.remove()

                //Positionnement camera
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13F))

                val cameraPosition = CameraPosition.Builder()
                    .target(latLng)
                    .zoom(15F)
                    //.bearing(90F)
                    //.tilt(40F)
                    .build()
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                if((departLocationReservation != null && destinationLocationReservation != null) && tabLocation.size> 1) {
//                    departMarkerReservation.remove()
//                    destinationMarkerReservation.remove()
                    tabLocation.clear()
                    if(departMarkerReservation != null)
                        departMarkerReservation!!.remove()
                    if(destinationMarkerReservation != null)
                        destinationMarkerReservation!!.remove()
                    //if(currentPolyline != null)
                       // currentPolyline.remove();
                }

                if(departLocationReservation != null && destinationLocationReservation != null){
                    destinationLocationReservation.latitude = latLng!!.latitude
                    destinationLocationReservation.longitude = latLng.longitude
                    tabLocation.add(destinationLocationReservation)
                    if(destinationMarkerReservation != null)
                        destinationMarkerReservation!!.remove()
                    addMarkerDestination(LatLng(latLng.latitude,latLng.longitude))

//                    Toast.makeText(context, ""+tabLocation.size(), Toast.LENGTH_SHORT).show();
                    if(departMarkerReservation != null && destinationMarkerReservation != null && tabLocation.size > 1) {
                        //showProgressDialog();
                        //M.setCurrentFragment("home",context);
                        //new FetchURL(getActivity(),"home").execute(getUrl(departMarkerReservation.getPosition(), destinationMarkerReservation.getPosition(), "driving"), "driving");
                    }
                }


            }

            override fun onError(status: Status) {

            }

        })


        //Choisir option ma position
        my_location!!.setOnClickListener {
            if (!isLocationEnabled(requireContext()))
                dialogMessageActiverGPS()
            else {
                if (currentLocation != null) {
                    //clean reservationMarker
                    if(departMarkerMesRequetes != null && destinationMarkerMesRequetes != null) {
                        departMarkerMesRequetes!!.remove()
                        destinationMarkerMesRequetes!!.remove()
                        //currentPolyline.remove();
                    }
                    val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13F))

                    val cameraPosition = CameraPosition.Builder()
                        //.bearing(90F)
                        //.tilt(40F)
                        .zoom(15F)
                        .target(latLng)
                        .build()
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    input_text_depart!!.setText(R.string.position)

                    //Geocoding Address
                    try {
                        val geo = Geocoder(requireContext(), Locale.getDefault())
                        val adresses = geo.getFromLocation(latLng.latitude, latLng.longitude, 1)
                        if (adresses.isEmpty()) {
                            Toast.makeText(
                                context,
                                "En attente de l'emplacement",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            if (adresses.size > 0) {
                                val adresse = adresses[0].getAddressLine(0)
                                if (adresse != "") {
                                    val tabAdress = adresse.split(",")
                                    input_text_depart!!.setText(tabAdress[0])
                                }
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    //Add Marker
                    if(destinationMarker != null)
                        destinationMarker!!.remove()
                    if((departLocationReservation != null && destinationLocationReservation != null) && tabLocation.size> 1) {
//                            departMarkerReservation.remove();
//                            destinationMarkerReservation.remove();
                        tabLocation.clear()
                        if(departMarkerReservation != null)
                            departMarkerReservation!!.remove()
                        if(destinationMarkerReservation != null)
                            destinationMarkerReservation!!.remove()
                        //if(currentPolyline != null)
                          //  currentPolyline.remove();
                    }
                    if(departLocationReservation != null && destinationLocationReservation != null){
                        departLocationReservation.latitude = latLng.latitude
                        departLocationReservation.longitude = latLng.longitude
                        tabLocation.add(departLocationReservation)
                        if(departMarkerReservation != null)
                            departMarkerReservation!!.remove()
                        addMarkerDepar(latLng)

                        //FetchUrl for direction
                    }

                }
            }
        }

        //Show PlacePiker
        chose_my_location!!.setOnClickListener {
            if(departLocationReservation !=null){
               val intent = PlacePicker.IntentBuilder()
                   .setLatLong(departLocationReservation.latitude,departLocationReservation.longitude) //init Map à l'entrer
                   .showLatLong(true) //afficher lat/long dans l'Activite
                   .setMapZoom(16f) //zoom par defaut est 14.0f
                   .setAddressRequired(true) //Définir si renvoyer uniquement les coordonnées si impossible de récupérer l'adresse pour les coordonnées. Par défaut: vrai
                   .hideMarkerShadow(true) // Masque l'ombre sous le marqueur de carte Départ. Par défaut: Faux
                   .setMarkerDrawable(R.drawable.ic_pin) // Change the default Marker Image
                   .setMarkerImageImageColor(R.color.grisGooglePlay)
                   .setFabColor(R.color.colorYellowDark)
                   .setPrimaryTextColor(R.color.colorLogoBlack) // Change text color of Shortened Address
                   .setSecondaryTextColor(R.color.colorLogoBlack) // Change text color of full Address
                   .setMapType(MapType.NORMAL)
                   .onlyCoordinates(true)  //Get only Coordinates from Place Picker
                   .build(requireActivity())

                startActivityForResult(intent, PLACE_PICKER_REQUEST_RESERVATION_DEPART)
            }else{
                val intent =PlacePicker.IntentBuilder()
                    .setLatLong(12.647378,-7.942810)  // Initial Latitude and Longitude the Map will load into
                    .showLatLong(true)  // Show Coordinates in the Activity
                    .setMapZoom(15.0f)  // Map Zoom Level. Default: 14.0
                    .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
                    .hideMarkerShadow(true) // Hides the shadow under the map markerDepart. Default: False
                    .setMarkerDrawable(R.drawable.ic_pin) // Change the default Marker Image
                    .setMarkerImageImageColor(R.color.grisGooglePlay)
                    .setFabColor(R.color.colorYellowDark)
                    .setPrimaryTextColor(R.color.colorLogoBlack) // Change text color of Shortened Address
                    .setSecondaryTextColor(R.color.colorLogoBlack) // Change text color of full Address
                    .setMapType(MapType.NORMAL)
                    .onlyCoordinates(true)  //Get only Coordinates from Place Picker
                    .build(requireActivity())
                startActivityForResult(intent, PLACE_PICKER_REQUEST_RESERVATION_DEPART)
            }
        }
        choose_my_location_destination !!.setOnClickListener {
            if(destinationLocationReservation !=null){
                val intent = PlacePicker.IntentBuilder()
                    .setLatLong(destinationLocationReservation.latitude,destinationLocationReservation.longitude) //init Map à l'entrer
                    .showLatLong(true) //afficher lat/long dans l'Activite
                    .setMapZoom(16f) //zoom par defaut est 14.0f
                    .setAddressRequired(true) //Définir si renvoyer uniquement les coordonnées si impossible de récupérer l'adresse pour les coordonnées. Par défaut: vrai
                    .hideMarkerShadow(true) // Masque l'ombre sous le marqueur de carte Départ. Par défaut: Faux
                    .setMarkerDrawable(R.drawable.ic_arrival_point_2) // Change the default Marker Image
                   // .setMarkerImageImageColor(R.color.grisGooglePlay)
                    .setFabColor(R.color.colorYellowDark)
                    .setPrimaryTextColor(R.color.colorLogoBlack) // Change text color of Shortened Address
                    .setSecondaryTextColor(R.color.colorLogoBlack) // Change text color of full Address
                    .setMapType(MapType.NORMAL)
                    .onlyCoordinates(true)  //Get only Coordinates from Place Picker
                    .build(requireActivity())

                startActivityForResult(intent, PLACE_PICKER_REQUEST_RESERVATION_DESTINATION)
            }else{
                val intent =PlacePicker.IntentBuilder()
                    .setLatLong(12.647378,-7.942810)  // Initial Latitude and Longitude the Map will load into
                    .showLatLong(true)  // Show Coordinates in the Activity
                    .setMapZoom(15.0f)  // Map Zoom Level. Default: 14.0
                    .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
                    .hideMarkerShadow(true) // Hides the shadow under the map markerDepart. Default: False
                    .setMarkerDrawable(R.drawable.ic_arrival_point) // Change the default Marker Image
                    .setMarkerImageImageColor(R.color.grisGooglePlay)
                    .setFabColor(R.color.colorYellowDark)
                    .setPrimaryTextColor(R.color.colorLogoBlack) // Change text color of Shortened Address
                    .setSecondaryTextColor(R.color.colorLogoBlack) // Change text color of full Address
//                        .setMapRawResourceStyle(R.raw.map_style)  //Set Map Style
                    .setMapType(MapType.NORMAL)
                    .onlyCoordinates(true)  //Get only Coordinates from Place Picker
                    .build(requireActivity())
                startActivityForResult(intent, PLACE_PICKER_REQUEST_RESERVATION_DESTINATION)
            }
        }

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
            val locationButton =
                (mapView!!.findViewById<View>("1".toInt()).parent as View).findViewById<View>("2".toInt())
            val layoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            layoutParams.setMargins(0, 20, 20, 550)
            val zoomButton =
                (mapView!!.findViewById<View>("1".toInt()).parent as View).findViewById<View>("1".toInt())
            val layoutParamsZoom = zoomButton.layoutParams as RelativeLayout.LayoutParams
            layoutParamsZoom.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            layoutParamsZoom.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            layoutParamsZoom.setMargins(0, 20, 20, 200)
        }


//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        //mMap.setOnMyLocationButtonClickListener()
        // mMap.setOnMyLocationClickListener(this)
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Initialize the location fields
        Log.d("Home currenLocation",currentLocation.toString())
        if (currentLocation != null) {
            val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(latLng, 13f)
            )
            val cameraPosition = CameraPosition.Builder()
                .target(latLng) // Sets the center of the map to location user
                .zoom(15f) // Sets the zoom
                //                    .bearing(90)                // Sets the orientation of the camera to east
                //                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build() // Creates a CameraPosition from the builder
          mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }

      mMap.setOnMapClickListener{
          Toast.makeText(context, ""+it.toString(), Toast.LENGTH_SHORT).show()
      }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PLACE_PICKER_REQUEST_RESERVATION_DEPART-> {
                if (!isLocationEnabled(requireContext()))
                    dialogMessageActiverGPS()
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PLACE_PICKER_REQUEST_RESERVATION_DEPART){
            if (resultCode == Activity.RESULT_OK && data!=null){
                if(departMarkerMesRequetes != null)
                    departMarkerMesRequetes!!.remove()
                if(destinationMarkerMesRequetes != null)
                    destinationMarkerMesRequetes!!.remove()

                val adresseData = data.getParcelableExtra<AddressData>(Constants.ADDRESS_INTENT)

                if(destinationMarker != null)
                    destinationMarker!!.remove()

                val latlong = LatLng(adresseData.latitude,adresseData.longitude)

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong,13f))
                val cameraPosition =CameraPosition.Builder()
                    .target(latlong)
                    .zoom(15f)
                    .build()
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                if((departLocationReservation != null && destinationLocationReservation != null) && tabLocation.size > 1) {
//                    departMarkerReservation.remove();
//                    destinationMarkerReservation.remove();
                    tabLocation.clear();
                    if(departMarkerReservation != null)
                        departMarkerReservation!!.remove();
                    if(destinationMarkerReservation != null)
                        destinationMarkerReservation!!.remove();
                    //if(currentPolyline != null)
                    // currentPolyline.remove();
                }

                if(departLocationReservation != null && destinationLocationReservation != null){
                    departLocationReservation.setLatitude(adresseData.latitude)
                    departLocationReservation.setLongitude(adresseData.longitude)
                    tabLocation!!.add(departLocationReservation);
                    if(departMarkerReservation != null)
                        departMarkerReservation!!.remove()
                    addMarkerDepar(LatLng(adresseData.latitude,adresseData.longitude));

                    if(departMarkerReservation != null && destinationMarkerReservation != null && tabLocation.size > 1) {
                        //showProgressDialog();
                        //M.setCurrentFragment("home",context);
                        // new FetchURL(getActivity(),"home").execute(getUrl(departMarkerReservation.getPosition(), destinationMarkerReservation.getPosition(), "driving"), "driving");
//                        BottomSheetFragmentRequeteFacturation bottomSheetFragmentBooking = new BottomSheetFragmentRequeteFacturation(getActivity(), departLocationReservation, destinationLocationReservation);
//                        bottomSheetFragmentBooking.show(((FragmentActivity) context).getSupportFragmentManager(), bottomSheetFragmentBooking.getTag());
                    }
                }
                input_text_depart!!.setText(requireContext().resources.getString(R.string.point_de_depart));

                try {
                    val geo = Geocoder(requireContext(),Locale.getDefault())
                    val addresses = geo.getFromLocation(latlong.latitude,latlong.longitude,1)
                    if(addresses.isNotEmpty()){
                        val address = addresses[0].getAddressLine(0)
                        if(address != ""){
                            val tabAdress = address.split(",")
                            input_text_depart!!.setText(tabAdress[0])
                        }
                    }
                }catch (e:IOException){
                    e.printStackTrace()
                }

            }else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status = Autocomplete.getStatusFromIntent(data!!);
                Log.i("HOME ERROR", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }else
            if(requestCode==PLACE_PICKER_REQUEST_RESERVATION_DESTINATION){
                if(resultCode==Activity.RESULT_OK && data!=null){
                    if(departMarkerMesRequetes != null)
                        departMarkerMesRequetes!!.remove()
                    if(destinationMarkerMesRequetes != null)
                        destinationMarkerMesRequetes!!.remove()

                    val adresseData = data.getParcelableExtra<AddressData>(Constants.ADDRESS_INTENT)

                    if(destinationMarker != null)
                        destinationMarker!!.remove()

                    val latlong = LatLng(adresseData.latitude,adresseData.longitude)

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong,13f))
                    val cameraPosition =CameraPosition.Builder()
                        .target(latlong)
                        .zoom(15f)
                        .build()
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                    if((departLocationReservation != null && destinationLocationReservation != null) && tabLocation.size > 1) {
                       departMarkerReservation!!.remove();
                       destinationMarkerReservation!!.remove();
                        tabLocation.clear();
                        if(departMarkerReservation != null)
                            departMarkerReservation!!.remove();
                        if(destinationMarkerReservation != null)
                            destinationMarkerReservation!!.remove();
                        //if(currentPolyline != null)
                        // currentPolyline.remove();
                    }
                    if(departLocationReservation != null && destinationLocationReservation != null){
                        destinationLocationReservation.latitude = adresseData.latitude;
                        destinationLocationReservation.longitude = adresseData.longitude;
                        tabLocation.add(departLocationReservation);
                        if(destinationMarkerReservation != null)
                            destinationMarkerReservation!!.remove();

                        addMarkerDestination(LatLng(adresseData.latitude,adresseData.longitude));

                        if(departMarkerReservation != null && destinationMarkerReservation != null && tabLocation.size > 1) {
                            //showProgressDialog();
                            //M.setCurrentFragment("home",context);
                            // new FetchURL(getActivity(),"home").execute(getUrl(departMarkerReservation.getPosition(), destinationMarkerReservation.getPosition(), "driving"), "driving");
//                        BottomSheetFragmentRequeteFacturation bottomSheetFragmentBooking = new BottomSheetFragmentRequeteFacturation(getActivity(), departLocationReservation, destinationLocationReservation);
//                        bottomSheetFragmentBooking.show(((FragmentActivity) context).getSupportFragmentManager(), bottomSheetFragmentBooking.getTag());
                        }
                    }
                    input_text_arrivee!!.setText(requireContext().resources.getString(R.string.destination))
                    try {
                        val geo = Geocoder(requireContext(),Locale.getDefault())
                        val addresses = geo.getFromLocation(latlong.latitude,latlong.longitude,1)
                        if(addresses.isNotEmpty()){
                            val address = addresses[0].getAddressLine(0)
                            if(address != ""){
                                val tabAdress = address.split(",")
                                input_text_arrivee!!.setText(tabAdress[0])
                            }
                        }
                    }catch (e:IOException){
                        e.printStackTrace()
                    }
                }else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    // TODO: Handle the error.
                    val status = Autocomplete.getStatusFromIntent(data!!);
                    Log.i("HOME ERROR", status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
            }else{
                super.onActivityResult(requestCode, resultCode, data)
            }

    }


    private fun addMarker(latLng: LatLng) {
        // Add Marker to Map
        val markerOptions = MarkerOptions()
        markerOptions.title(resources.getString(R.string.depart))
        markerOptions.snippet(resources.getString(R.string.point_depart))
       Log.d("Home LatLng",latLng.toString())
        markerOptions.position(latLng)
        markerOptions.icon(generateBitmapDescriptorFromRes(requireContext(), R.drawable.ic_location_pin_1))
        departMarkerReservation = mMap.addMarker(markerOptions)
        departMarkerReservation!!.tag = resources.getString(R.string.depart)
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
    private fun addMarkerDestination(latLng: LatLng) {
        //Ajouter Marker
        val markerOptions = MarkerOptions()
        markerOptions.title(resources.getString(R.string.arrivee))
        markerOptions.snippet(resources.getString(R.string.point_depart))
        markerOptions.position(latLng)
        markerOptions.icon(generateBitmapDescriptorFromRes(requireContext(), R.drawable.ic_arrival_point_2))
        destinationMarkerReservation = mMap.addMarker(markerOptions)
        destinationMarkerReservation!!.tag = resources.getString(R.string.arrive)
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

    //Verifier que c'st activer le GPS
    private fun isLocationEnabled(context: Context): Boolean {
        var enable = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val mode = Settings.Secure.getInt(
                context.contentResolver, Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )

            enable = (mode != Settings.Secure.LOCATION_MODE_OFF)
        } else {
            val service = context.getSystemService(LOCATION_SERVICE) as LocationManager
            enable = service.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    service.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }
        return enable
    }

    //Show Message pour activer le GPS
    private fun dialogMessageActiverGPS() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(R.string.gps_active_servce)
            .setCancelable(false)
            .setPositiveButton("OUI") { dialogInterface: DialogInterface, i: Int ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("NON") { dialog, which ->
                dialog.dismiss()

            }
        val alert = builder.create()
        alert.show()
    }

    //Google Api start Location Or Update Request and callBack
    private fun createLocationRequest(){
        locationRequest= LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = UPDATE_INTERVAL
        locationRequest.fastestInterval = FAST_INTERVAL
        locationRequest.smallestDisplacement = 20f

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(requireContext())
        val task =  client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener {e ->  
          if(e is ResolvableApiException){
              try {
                  e.startResolutionForResult(requireActivity(),REQUEST_CHECK_SETTINGS)
              }catch (sendex: IntentSender.SendIntentException){

              }
          }
        }

    }

    private fun startLocationUpdates() {
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
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,
            Looper.myLooper())
    }

    private fun  startLocationOrUpdateCallBack(){
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                currentLocation = p0!!.lastLocation
                Log.d("Home current",p0.lastLocation.toString())

            }

        }
    }




}

