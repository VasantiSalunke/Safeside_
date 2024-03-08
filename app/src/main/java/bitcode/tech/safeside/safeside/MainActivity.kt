package bitcode.tech.safeside.safeside

import android.provider.Settings
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import bitcode.tech.safeside.safeside.databinding.ActivityMainBinding
import bitcode.tech.safeside.safeside.fragments.HomeGoogleMapFragment
import bitcode.tech.safeside.safeside.viewmodels.LocationPickerFragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        initializedApp(savedInstanceState)

        if (isLocationPermissionGranted()) {

            initializedApp(savedInstanceState)
            Log.d("MainActivity", "Location permission is already granted.")
        } else {

            requestLocationPermission()
            Log.d("MainActivity","Request Location permission..")

        }
    }



    private fun initializedApp(savedInstanceState : Bundle?) {

        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigationView)

        setSupportActionBar(toolbar)


        val reportButton = findViewById<Button>(R.id.btnReport)

        reportButton.setOnClickListener {
            val locationPickerFragment = LocationPickerFragment()

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentcontainer, locationPickerFragment)
                .addToBackStack(null)
                .commit()
        }

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()
            true
        }

        supportActionBar?.title = "Safeside"


        if (savedInstanceState == null) {
            showHomeGoogleMapFragment()
        }

    }

    override fun onResume() {
        super.onResume()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                initializedApp(null)

            } else {

                showLocationPermissionDeniedDialog()
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun showLocationPermissionDeniedDialog() {

        val dialog = AlertDialog.Builder(this)
            .setTitle("Location Permission Required")
            .setMessage("Please grant location permission to use this app.")
            .setPositiveButton("Grant") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()

            }
            .setCancelable(false)
            .create()
        dialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showHomeGoogleMapFragment() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.mapFragment, HomeGoogleMapFragment())
            .commit()

        enableDrawer()
    }

    fun showAnomalyDetailsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mapLinearLayout, AnomalyDetailsFragment())
            .commit()


        disableDrawer()
    }

    private fun enableDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    fun disableDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }


}
