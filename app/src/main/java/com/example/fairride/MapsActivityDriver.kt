package com.example.fairride

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
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
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.firebase.client.DataSnapshot
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import com.google.android.gms.location.*
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
    lateinit var addressResponse: JSONObject
    lateinit var fromResponse: JSONObject
    lateinit var ref : DatabaseReference
    lateinit var destination: String
    lateinit var from: String
    lateinit var currentRouteId: String
    lateinit var currentRoute: Route



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Touch destination on the map")
        setContentView(R.layout.activity_maps_driver)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        ref = FirebaseDatabase.getInstance().getReference("routes")
        val bundle = intent.extras
        val consumption = bundle.getDouble("consumption")
        val username = bundle.getString("username")
        currentRouteId = ref.push().key!!

        ref.addValueEventListener(object : ValueEventListener, com.google.firebase.database.ValueEventListener {
            override fun onCancelled(p0: FirebaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {

            }

            override fun onDataChange(ds: com.google.firebase.database.DataSnapshot) {
                if (ds!!.exists()) {
                    for (e in ds.children) {
                        if ( e.key == currentRouteId) {
                            currentRoute = e.getValue(Route::class.java)!!
                        }
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {
            }

        })

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        sendRouteButton.setOnClickListener {
            // add modified json to firebase
            println("sendRouteButton")
            jsonResponse.put("username", username)
            jsonResponse.put("consumption", consumption)
            println(jsonResponse)

            val route = Route(consumption.toString(), username, from, destination)
            ref.child(currentRouteId).setValue(route)
            // toast nie dziala wtf
            Toast.makeText(this@MapsActivityDriver, "Route confirmed", Toast.LENGTH_SHORT)
            sendRouteButton.visibility = View.GONE
            distanceText.visibility = View.GONE
            timeText.visibility = View.GONE
            endButton.visibility = View.VISIBLE
            setTitle("Ready to go!")
        }

        endButton.setOnClickListener {
            val routeAdd = Route(currentRoute)
            var divideBy = 1
            val consumption = currentRoute.consumption!!.toDouble()
            lateinit var newCost1: String
            lateinit var newCost2: String
            lateinit var newCost3: String
            lateinit var newCost4: String
            val intent = Intent(this, DriverSummaryActivity::class.java)

            if (currentRoute.pass1inout == "in") {
                divideBy = divideBy + 1
                routeAdd.pass1End = currentLocation.toString().substring(10,30)
            }
            if (currentRoute.pass2inout == "in") {
                divideBy = divideBy + 1
                routeAdd.pass2End = currentLocation.toString().substring(10,30)
            }
            if (currentRoute.pass3inout == "in") {
                divideBy = divideBy + 1
                routeAdd.pass3End = currentLocation.toString().substring(10,30)
            }
            if (currentRoute.pass4inout == "in") {
                divideBy = divideBy + 1
                routeAdd.pass4End = currentLocation.toString().substring(10,30)
            }

            val distance = getDistance(currentRoute.lastCheckpoint!!, currentLocation.toString())
            var cost = ((distance/100000) * consumption / divideBy) * 5.20
            if (currentRoute.pass1cost != "" && currentRoute.pass1inout == "in") {
                newCost1 = (cost + currentRoute.pass1cost!!.toDouble()).toString()
                routeAdd.pass1cost = newCost1
                intent.putExtra("cost1", newCost1)
            } else if (currentRoute.pass1cost != "" && currentRoute.pass1inout == "out") {
                intent.putExtra("cost1", currentRoute.pass1cost)
            } else {
                intent.putExtra("cost1", "")
            }
            if (currentRoute.pass2cost != "" && currentRoute.pass2inout == "in") {
                newCost2 = (cost + currentRoute.pass2cost!!.toDouble()).toString()
                routeAdd.pass2cost = newCost2
                intent.putExtra("cost2", newCost2)
            } else if (currentRoute.pass2cost != "" && currentRoute.pass2inout == "out") {
                intent.putExtra("cost2", currentRoute.pass2cost)
            } else {
                intent.putExtra("cost2", "")
            }
            if (currentRoute.pass3cost != "" && currentRoute.pass3inout == "in") {
                newCost3 = (cost + currentRoute.pass3cost!!.toDouble()).toString()
                routeAdd.pass3cost = newCost3
                intent.putExtra("cost3", newCost3)
            } else if (currentRoute.pass3cost != "" && currentRoute.pass3inout == "out") {
                intent.putExtra("cost3", currentRoute.pass3cost)
            } else {
                intent.putExtra("cost3", "")
            }
            if (currentRoute.pass4cost != "" && currentRoute.pass4inout == "in") {
                newCost4 = (cost + currentRoute.pass4cost!!.toDouble()).toString()
                routeAdd.pass4cost = newCost4
                intent.putExtra("cost4", newCost4)
            } else if (currentRoute.pass4cost != "" && currentRoute.pass4inout == "out") {
                intent.putExtra("cost4", currentRoute.pass4cost)
            } else {
                intent.putExtra("cost4", "")
            }

            if (currentRoute.pass1inout == "in") {
                routeAdd.pass1inout = "out"
            }
            if (currentRoute.pass2inout == "in") {
                routeAdd.pass2inout = "out"
            }
            if (currentRoute.pass3inout == "in") {
                routeAdd.pass3inout = "out"
            }
            if (currentRoute.pass4inout == "in") {
                routeAdd.pass4inout = "out"
            }
            routeAdd.lastCheckpoint = currentLocation.toString().substring(10,30)
            ref.child(currentRouteId!!).setValue(routeAdd)

            if (currentRoute.pass1 != "") {
                intent.putExtra("user1", currentRoute.pass1)
            } else {
                intent.putExtra("user1", "")
            }
            if (currentRoute.pass1 != "") {
                intent.putExtra("user2", currentRoute.pass2)
            } else {
                intent.putExtra("user2", "")
            }
            if (currentRoute.pass1 != "") {
                intent.putExtra("user3", currentRoute.pass3)
            } else {
                intent.putExtra("user3", "")
            }
            if (currentRoute.pass1 != "") {
                intent.putExtra("user4", currentRoute.pass4)
            } else {
                intent.putExtra("user4", "")
            }
            startActivity(intent)
            // przejscie do nowego activity
            // a tam pobranie trasy, policzenie i podsumowanie
            // i jeszcze otagowanie wpisu w bazie za zakonczony zeby uzytkownik mial co pobrac

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

    private fun getDistance(checkpoint: String, currentLoc: String): Double {
        val distance = 5000.0
        return distance
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

                    val urlAddress = "https://api.openrouteservice.org/geocode/reverse?api_key=5b3ce3597851110001cf6248618d9768f9db4f1d8b5951a97bd8abf3&point.lon=${latLng.longitude}&point.lat=${latLng.latitude}&boundary.circle.radius=5&size=1&layers=address"
                    val addressRequest = object : StringRequest(
                        Request.Method.GET, urlAddress, Response.Listener<String> {
                                response ->
                            try {
                                addressResponse = JSONObject(response)
                                val features = addressResponse.getJSONArray("features")
                                val properties = features.getJSONObject(0).getJSONObject("properties")
                                val street = properties.getString("street")
                                val housenumber = properties.getString("housenumber")
                                val locality = properties.getString("locality")
                                destination = street + " " + housenumber + ", " + locality
                                println(destination)
                                mMap.addMarker(MarkerOptions().position(latLng).title(destination)).showInfoWindow()
                            } catch (t: Throwable) {
                                destination = "-"
                            }
                        }, Response.ErrorListener {
                                _ ->

                        }){}

                    val urlFrom = "https://api.openrouteservice.org/geocode/reverse?api_key=5b3ce3597851110001cf6248618d9768f9db4f1d8b5951a97bd8abf3&point.lon=${currentLocation.longitude}&point.lat=${currentLocation.latitude}&boundary.circle.radius=5&size=1&layers=address"
                    val fromRequest = object : StringRequest(
                        Request.Method.GET, urlFrom, Response.Listener<String> {
                                response ->
                            try {
                                fromResponse = JSONObject(response)
                                val features = fromResponse.getJSONArray("features")
                                val properties = features.getJSONObject(0).getJSONObject("properties")
                                val street = properties.getString("street")
                                val housenumber = properties.getString("housenumber")
                                val locality = properties.getString("locality")
                                from = street + " " + housenumber + ", " + locality
                                println(from)
                            } catch (t: Throwable) {
                                from = "-"
                            }
                        }, Response.ErrorListener {
                                _ ->

                        }){}

                    val requestQueue = Volley.newRequestQueue(applicationContext)
                    requestQueue.add(directionsRequest)
                    requestQueue.add(addressRequest)
                    requestQueue.add(fromRequest)
                    sendRouteButton.visibility = View.VISIBLE
                }

            })

        }

    }

}
