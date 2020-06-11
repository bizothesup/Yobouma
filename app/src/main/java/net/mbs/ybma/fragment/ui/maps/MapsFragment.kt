package net.mbs.ybma.fragment.ui.maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import net.mbs.ybma.R

class MapsFragment : Fragment(),OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    //Map
    var currentLocation: Location? = null


    //FlottingButton Action
    var destinationMarker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        //mapView = mapFragment!!.view
        mapFragment!!.getMapAsync(this)
    }

    //addMarker
    private fun addMarker(latLng: LatLng){
        val options = MarkerOptions()
        options.title("Destination")
        options.snippet("Tu veux aller ici")
        options.position(latLng)
        options.icon(generateBitmapDescriptorFromRes(requireContext(), R.drawable.ic_location_pin_1))
        destinationMarker = mMap.addMarker(options)
        destinationMarker!!.tag = requireContext().resources.getString(R.string.destination)
    }

    private fun generateBitmapDescriptorFromRes(context: Context?, resId: Int): BitmapDescriptor? {
        val drawable =ContextCompat.getDrawable(requireContext(),resId)
        drawable!!.setBounds(
            0,
            0,
            drawable.intrinsicWidth,
            drawable.intrinsicHeight)

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888)

        val canvas=Canvas(bitmap)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requireContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && requireContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        mMap.isMyLocationEnabled = true

        val sydney = LatLng(-34.0, 151.0)
        addMarker(sydney)
        // googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}