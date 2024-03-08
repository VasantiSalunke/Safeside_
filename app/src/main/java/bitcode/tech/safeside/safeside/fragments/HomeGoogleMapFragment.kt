package bitcode.tech.safeside.safeside.fragments


import  android.annotation.SuppressLint
import android.content.Intent
import com.google.android.gms.location.FusedLocationProviderClient
import android.content.pm.PackageManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import bitcode.tech.safeside.safeside.AnomalyDetailsFragment
import bitcode.tech.safeside.safeside.AnomalyLocationTrackingService
import bitcode.tech.safeside.safeside.R
import bitcode.tech.safeside.safeside.databinding.GoogleMapFragmentBinding
import bitcode.tech.safeside.safeside.factory.AnomaliesViewModelFactory
import bitcode.tech.safeside.safeside.network.AnomaliesApiService
import bitcode.tech.safeside.safeside.repositories.AnomaliesRepository
import bitcode.tech.safeside.safeside.viewmodels.AnomaliesViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import java.io.Serializable
import kotlin.math.absoluteValue

class HomeGoogleMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private lateinit var binding: GoogleMapFragmentBinding
    private lateinit var anomaliesViewmodel: AnomaliesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = GoogleMapFragmentBinding.inflate(inflater, container, false)


        initViewModel()
        initObserver()



        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.btnTrackAnomalies.setOnClickListener {
            startForegroundService()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        mt("Map Ready")

        anomaliesViewmodel.getAnomalies()

        /* map = googleMap
         googleMap.isMyLocationEnabled = true


         val indiaLatLng = LatLng(20.5937, 78.9629)
         val zoomLevel = 5.0f
         val cameraPosition = CameraPosition.Builder().target(indiaLatLng).zoom(zoomLevel).build()

         map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
 */


        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getDeviceLocation()
            initMap()
            map.isMyLocationEnabled = true

//            map.setOnMyLocationChangeListener { location ->
//                anomaliesViewmodel.getAnomalies()

        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun initMap() {
        map.setOnInfoWindowClickListener { marker ->
            val anomalyDetailsFragment = AnomalyDetailsFragment()

            val inputBundle = Bundle()
            inputBundle.putSerializable("anomaly", marker.tag as Serializable)
            anomalyDetailsFragment.arguments = inputBundle


            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentcontainer, anomalyDetailsFragment, null)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initMap()
                getDeviceLocation()

            } else {
                mt("Location Permission Denied")
            }
        }
    }


    private fun startForegroundService() {
        val serviceIntent = Intent(requireContext(), AnomalyLocationTrackingService::class.java)
        ContextCompat.startForegroundService(requireActivity().applicationContext, serviceIntent)

    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
//                    map.addMarker(MarkerOptions().position(currentLatLng).title("Your Location"))
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM))
                } else {
                    mt("Unable to get current location. Make sure location is enabled on the device.")
                }
            }
    }

    private fun mt(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val DEFAULT_ZOOM = 15.0f
    }

    private fun initViewModel() {
        anomaliesViewmodel = ViewModelProvider(
            this,
            AnomaliesViewModelFactory(
                AnomaliesRepository(AnomaliesApiService.getInstance())
            )
        )[AnomaliesViewModel::class.java]

    }

    private fun initObserver() {
        anomaliesViewmodel.anomaliesLoadedLiveData.observe(viewLifecycleOwner) { loaded ->

            val random = java.util.Random()

            if (loaded) {
                for (anomaly in anomaliesViewmodel.anomaliesList) {
                    val marker = map.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                anomaly.lat + random.nextDouble().absoluteValue,
                                anomaly.lng + random.nextDouble().absoluteValue
                            )
                        ).title(anomaly.title)
                    )
                    marker!!.tag = anomaly
                }

            } else {

                mt("Failed to load anomalies.")
            }

        }
    }

}