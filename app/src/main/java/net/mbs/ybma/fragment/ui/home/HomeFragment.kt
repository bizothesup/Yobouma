package net.mbs.ybma.fragment.ui.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import net.mbs.ybma.R

class HomeFragment : Fragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener,
    OnMyLocationClickListener,
    OnMyLocationButtonClickListener{


    private lateinit var homeViewModel: HomeViewModel

    //GoogleMap
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    /** GOOGLE API CLIENT **/
    private lateinit var mGoogleApiClient: GoogleApiClient

    /** View **/
    private var locationManager: LocationManager? = null
    private var provider:String?=null
    private var verif = false

    //valeur statis
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private lateinit var currentLocation: Location
        private const val  UPDATE_INTERVAL : Long= 5000
        private const val FASTEST_INTERVAL: Long = 5000
        var currentMarker: Marker? = null
        private val context: Context? = null
        var activity: Activity? = null
    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect()
        }
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
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
//            return;
        }
        //Construire le google api client
        mGoogleApiClient = GoogleApiClient.Builder(requireContext())
            //                .enableAutoManage(getActivity(),0,this)
            .addApi(LocationServices.API)
//                .addApi(Places.GEO_DATA_API)
            .addConnectionCallbacks(this)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(getActivity(), this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this).build()

        //Location Manager
        locationManager =  requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        val criteria=Criteria()
        provider = locationManager!!.getBestProvider(criteria,false)
        if(provider != null){
           // currentLocation =locationManager!!.getLastKnownLocation(provider)
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
        // Add a marker in Sydney and move the camera
        mMap.uiSettings.isZoomControlsEnabled = true


    }

    //Methode Start MAp
    private fun startMap() {
       val  locationRequest=LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = UPDATE_INTERVAL
        locationRequest.fastestInterval= FASTEST_INTERVAL
        //verfification des permission
        if (ActivityCompat.checkSelfPermission(
               requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
//            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient,
            locationRequest,
            this
        )
    }

    private fun placeMarkerOnMap(currentLatLng: LatLng) {
        Log.d("CURRENTMAP",currentLatLng.toString())
        val markerOptions = MarkerOptions().position(currentLatLng)
        mMap.addMarker(markerOptions)
    }


    override fun onConnected(p0: Bundle?) {
       /* if (ActivityCompat.checkSelfPermission(
               requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        // Permissions ok, we get last location

        // Permissions ok, we get last location
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
*/
        startMap()
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }

    override fun onLocationChanged(location: Location?) {
       /* currentLocation = location!!
//        Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();
        //        Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();
        if (verif == false) {
//            destinationLocation = location;
//            departLocationReservation = location;
//            destinationLocationReservation = location;

            // Initialize the location fields
            if (currentLocation != null) {
                val latLng = LatLng(
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude()
                )
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(latLng, 13f)
                )
                val cameraPosition = CameraPosition.Builder()
                    .target(latLng) // Sets the center of the map to location user
                    .zoom(15f) // Sets the zoom
                    //                    .bearing(90)                // Sets the orientation of the camera to east
                    //                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build() // Creates a CameraPosition from the builder
               mMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(cameraPosition)
                )
            }
            verif = true
        }

        if (location != null) {
            if (currentMarker != null)
                currentMarker!!.remove()
            //if (!SessionUser.getUserCategorie(requireContext()).equals("client"))
              // setCurrentLocation().execute(location.latitude.toString(), location.longitude.toString())
        }*/
    }

    override fun onMyLocationClick(p0: Location) {
        TODO("Not yet implemented")
    }

    override fun onMyLocationButtonClick(): Boolean {

//        Toast.makeText(mContext, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
       // if (!isLocationEnabled(requireContext()))
            //showMessageEnabledGPS()
        return false
    }


   /* override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (!isLocationEnabled(requireContext()))
                    showMessageEnabledGPS()
                return
            }
        }
    }

    private fun showMessageEnabledGPS() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(
            requireContext().getResources()
                .getString(R.string.this_service_requires_the_activation_of_the_gps)
        )
            .setCancelable(false)
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    startActivity(
                        Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS
                        )
                    )
                })
            .setNegativeButton("No",
                DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.cancel() })
        val alert = builder.create()
        alert.show()
    }

    //Active GPS
    private fun isLocationEnabled(context: Context):Boolean{
    // String locationProviders;
        var enabled = false
        enabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val mode = Settings.Secure.getInt(
                context.contentResolver, Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )
            mode != Settings.Secure.LOCATION_MODE_OFF
        } else {
            val service =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            service.isProviderEnabled(LocationManager.GPS_PROVIDER) || service.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }
        return enabled
    }*/
}

