package bitcode.tech.safeside.safeside.viewmodels

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import bitcode.tech.safeside.safeside.R
import bitcode.tech.safeside.safeside.databinding.LocationPickerFragmentBinding

import bitcode.tech.safeside.safeside.fragments.ReportAnomalyFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class LocationPickerFragment : Fragment(), OnMapReadyCallback {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private lateinit var binding: LocationPickerFragmentBinding
    private var currentLocationMarker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LocationPickerFragmentBinding.inflate(inflater, container, false)
        mapView = binding.currentLocationMap
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        initListeners()

        return binding.root
    }

    private fun initListeners() {


        val reportAnomalyFragment = ReportAnomalyFragment()

        binding.btnAddDetails.setOnClickListener {

            currentLocationMarker?.let { marker ->
                val latLng = marker.position
                val bundle = Bundle().apply {
                    putDouble("latitude", latLng.latitude)
                    putDouble("longitude", latLng.longitude)

                }
                Log.d("tag","${latLng.latitude},${latLng.longitude}")


                reportAnomalyFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentcontainer, reportAnomalyFragment)
                    .addToBackStack(null)
                    .commit()

            }
        }


    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }



    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        map.isMyLocationEnabled = true

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: android.location.Location? ->
                location?.let {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    map.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            currentLatLng,
                            DEFAULT_ZOOM
                        )
                    )


                    currentLocationMarker = map.addMarker(
                        MarkerOptions().position(currentLatLng).title("Current Location")
                            .draggable(true)
                    )

                    Log.d("MarkerPosition", "Initial Marker LatLng: $currentLatLng")
                }
            }

        map.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {

            }

            override fun onMarkerDrag(marker: Marker) {

            }

            override fun onMarkerDragEnd(marker: Marker) {
                val updatedLatLng = marker.position
                Log.d("MarkerPosition", "Updated Marker LatLng: $updatedLatLng")
            }
        })
    }


    companion object {
        private const val DEFAULT_ZOOM = 15f
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }
}