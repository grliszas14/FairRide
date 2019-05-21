package com.example.fairride

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.jar.Manifest
import kotlin.system.exitProcess

private const val PERMISSION_REQUEST = 10
class MapsActivityDriver : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION)
    lateinit var locationManager: LocationManager
    private var hasGPS = false
    private var hasNetwork = false
    private var locationGPS : Location? = null
    private var locationNetwork: Location? = null
    lateinit var currentLocation: LatLng



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_driver)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)




    }


    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        println(hasGPS)
        println(hasNetwork)
        if (hasGPS || hasNetwork) {
            if (hasGPS) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, object: LocationListener  {
                    override fun onLocationChanged(p0: Location?) {
                        if(p0 != null) {
                            locationGPS = p0
                        }

                    }

                    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

                    }

                    override fun onProviderEnabled(p0: String?) {

                    }

                    override fun onProviderDisabled(p0: String?) {

                    }

                })
                val localGPSLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if(localGPSLocation != null) {
                    locationGPS = localGPSLocation
                }
            }
            if (hasNetwork) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, object: LocationListener   {
                    override fun onLocationChanged(p0: Location?) {
                        if(p0 != null) {
                            locationNetwork = p0
                        }

                    }

                    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

                    }

                    override fun onProviderEnabled(p0: String?) {

                    }

                    override fun onProviderDisabled(p0: String?) {

                    }

                })
                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if(localNetworkLocation != null) {
                    locationGPS = localNetworkLocation
                }
            }

            if (locationGPS != null && locationNetwork != null) {
                if (locationGPS!!.accuracy > locationNetwork!!.accuracy) {
                    println(locationGPS!!.latitude)
                    println(locationGPS!!.longitude)
                } else {
                    println(locationNetwork!!.latitude)
                    println(locationNetwork!!.longitude)
                }
            }
            if (locationGPS != null) {
                println(locationGPS!!.latitude)
                println(locationGPS!!.longitude)

            }
            if (locationNetwork !=null) {
                println(locationNetwork!!.latitude)
                println(locationNetwork!!.longitude)
            }
        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            getLocation()
        }
    }

    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED) {
                allSuccess = false
            }
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            var allSuccess = true
            for(i in permissions.indices) {
                if(grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                }
            }
            if(allSuccess) {
                println("Permission granted")
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (checkPermission(permissions)) {
            getLocation()
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissions(permissions, PERMISSION_REQUEST)
            //if (!checkPermission(permissions)) {
            //    exitProcess(-1)
            //}
        }

        // Add a marker in Sydney and move the camera
        //val przasnysz = LatLng(53.02, 20.88)
        var current = LatLng(53.02, 20.88)
        while(locationGPS == null) {
            getLocation()
            current = LatLng(locationGPS!!.latitude, locationGPS!!.longitude)
        }
        //mMap.addMarker(MarkerOptions().position(current).title("Marker in current location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 16.0f))

    }
}
