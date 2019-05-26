package com.example.fairride

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.*
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_maps_driver.*
import org.json.JSONObject
import java.util.jar.Manifest
import kotlin.math.round
import kotlin.system.exitProcess


class MapsActivityDriver : AppCompatActivity(), OnMapReadyCallback {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    private lateinit var mMap: GoogleMap
    lateinit var currentLocation: LatLng
    val REQUEST_CODE = 1000;
    lateinit var jsonResponse: JSONObject




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Touch destination on the map")
        setContentView(R.layout.activity_maps_driver)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val bundle = intent.extras
        val consumption = bundle.getDouble("consumption")
        val username = bundle.getString("username")

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        sendRouteButton.isClickable = false
        sendRouteButton.setOnClickListener {
            // add modified json to firebase
            println("sendRouteButton")
            jsonResponse.put("username", username)
            jsonResponse.put("consumption", consumption)
            println(jsonResponse)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            REQUEST_CODE->{
                if(grantResults.size > 0) {
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        Toast.makeText(this@MapsActivityDriver, "Permission granted", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this@MapsActivityDriver, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun buildLocationCallback() {
        locationCallback = object: LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                var location = p0!!.locations.get(p0!!.locations.size-1)
                currentLocation = LatLng(location!!.latitude, location!!.longitude)
                println("Elo")
            }
        }
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
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
        var current = LatLng(53.02, 20.88)
        currentLocation = LatLng(53.02, 20.88)
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                currentLocation = LatLng (location!!.latitude, location!!.longitude)
                println("ELO")
                println(currentLocation)
                mMap.isMyLocationEnabled = true
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16.0f))
            }
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
        } else {
            buildLocationRequest()
            buildLocationCallback()


            if(ActivityCompat.checkSelfPermission(this@MapsActivityDriver, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this@MapsActivityDriver, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this@MapsActivityDriver, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null /*Looper.myLooper()*/)
            /*if (currentLocation != null) {
                println("ELO")
                println(currentLocation)
                mMap.isMyLocationEnabled = true
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16.0f))
            }*/

            mMap!!.setOnMapClickListener(object: GoogleMap.OnMapClickListener {
                override fun onMapClick(latLng: LatLng) {
                    mMap.clear()
                    val path: MutableList<LatLng> = ArrayList()
                    val urlDirections = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=5b3ce3597851110001cf6248618d9768f9db4f1d8b5951a97bd8abf3&start=${currentLocation.longitude},${currentLocation.latitude}&end=${latLng.longitude},${latLng.latitude}"
                    println(urlDirections)
                    val directionsRequest = object : StringRequest(
                        Request.Method.GET, urlDirections, Response.Listener<String> {
                                response ->
                            //val jsonResponse = JSONObject(response)
                            jsonResponse = JSONObject(response)
                            // Get and draw routes
                            val features = jsonResponse.getJSONArray("features")
                            val geometry = features.getJSONObject(0).getJSONObject("geometry")
                            val coordinatesArray = geometry.getJSONArray("coordinates")
                            println(coordinatesArray)
                            for (i in 0 until coordinatesArray.length()) {
                                val coords = coordinatesArray.getJSONArray(i)
                                val latitude = coords.getString(0)
                                val longtitude = coords.getString(1)
                                val pointLatLng = LatLng(longtitude.toDouble(), latitude.toDouble())
                                path.add(pointLatLng)
                            }
                            for (i in 0 until path.size-1) {
                                //mMap!!.addPolyline(PolylineOptions().addAll(path).color(Color.RED))
                                mMap!!.addPolyline(PolylineOptions().add(path[i],path[i+1]).color(Color.RED))

                            }

                            // Get time and distance
                            val properties = features.getJSONObject(0).getJSONObject("properties")
                            val summary = properties.getJSONObject("summary")

                            // Format distance
                            var meterUnit = "km"
                            var distance = summary.getString("distance").toDouble()/1000
                            if (distance > 1.0) {
                                distance = distance * 10
                                distance = round(distance)
                                distance = distance / 10
                            } else {
                                distance = round(distance * 1000)
                                meterUnit = "m"
                            }

                            val distanceText: TextView = findViewById(R.id.distanceText) as TextView
                            distanceText.text = "Distance: " + distance.toString() + meterUnit

                            // Format time
                            val timeText: TextView = findViewById(R.id.timeText) as TextView
                            val time = round(summary.getString("duration").toDouble()).toInt()
                            if ( time < 60) {
                                timeText.text = "Time: " + time.toString() + "s"
                            } else if(time > 60 && time < 3600) {
                                val seconds = time % 60
                                val minutes = (time - seconds) / 60
                                timeText.text = "Time: " + minutes.toString() + "m " + seconds + "s"
                            } else if(time > 3600) {
                                val seconds = time % 60
                                val minutes = ((time - seconds) % 3600) / 60
                                val hours = (time - seconds - minutes * 60) / 3600
                                timeText.text = "Time: " + hours.toString() + "h " + minutes.toString() + "m " + seconds.toString() + "s"
                            }


                        }, Response.ErrorListener {
                                _ ->
                        }){}
                    val requestQueue = Volley.newRequestQueue(applicationContext)

                    requestQueue.add(directionsRequest)
                    sendRouteButton.isClickable = true
                }

            })

        }

    }
}
