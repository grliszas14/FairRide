package com.example.fairride

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.SupportActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.firebase.client.DataSnapshot
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_passenger.*
import org.json.JSONObject
import org.w3c.dom.Text

class PassengerActivity : AppCompatActivity() {


    lateinit var ref : DatabaseReference
    lateinit var keyList: ArrayList<String>
    lateinit var routesList: ArrayList<Route>
    var ifIsInRoute: Boolean = false
    lateinit var currentRoute: Route
    lateinit var currentRouteId: String
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    lateinit var currentLocation: LatLng
    lateinit var currentLocationS: String
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var whichPass: String





    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        ref = FirebaseDatabase.getInstance().getReference("routes")
        var username = FirebaseAuth.getInstance().currentUser!!.displayName
        keyList = arrayListOf()
        routesList = arrayListOf()
        val listView = findViewById<ListView>(R.id.listViewRoutes)
        var routeInfo = findViewById<LinearLayout>(R.id.routeInfo)
        currentLocation = LatLng(53.02, 20.88)
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                currentLocation = LatLng (location!!.latitude, location!!.longitude)
            }
        buildLocationRequest()
        buildLocationCallback()
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null /*Looper.myLooper()*/)

        ref.addValueEventListener(object : ValueEventListener, com.google.firebase.database.ValueEventListener {
            override fun onCancelled(p0: FirebaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {

            }

            override fun onDataChange(ds: com.google.firebase.database.DataSnapshot) {
                if (ds!!.exists()) {
                    keyList.clear()
                    for (e in ds.children) {
                        keyList.add(e.key!!)
                        println(keyList)
                    }
                    routesList.clear()
                    for (e in ds.children) {
                        routesList.add(e.getValue(Route::class.java)!!)
                        println(routesList)
                    }
                    listView.adapter = ListViewRouteAdapter(applicationContext, routesList)

                }

            }

            override fun onCancelled(p0: DatabaseError) {
            }

        })
        endRoute_button.setOnClickListener{
            routeInfo.setVisibility(View.INVISIBLE)
            ifIsInRoute = false
            val routeAdd = Route(currentRoute)
            currentLocationS = currentLocation.toString().substring(10,30)
            println(currentLocationS)
            when (whichPass) {
                "pass1" -> {
                    routeAdd.pass1End = currentLocationS
                }
                "pass2" -> {
                    routeAdd.pass2End = currentLocationS
                }
                "pass3" -> {
                    routeAdd.pass3End = currentLocationS
                }
                "pass4" -> {
                    routeAdd.pass4End = currentLocationS
                }
                else -> false
            }
            ref.child(currentRouteId!!).setValue(routeAdd)
            whichPass = ""

        }
        listView.setOnItemClickListener { parent, view, position, id ->
            val popup = PopupMenu(this, view)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId){
                    R.id.startRide_popup -> {
                        currentRoute = routesList[position]
                        currentRouteId = keyList[position]
                        currentLocationS = currentLocation.toString().substring(10,30)
                        routeInfo.findViewById<TextView>(R.id.route_textView).text = currentRoute.destination
                        if (currentRoute.pass1 == "") {
                            val routeAdd = Route(currentRoute)
                            routeAdd.pass1 = username
                            routeAdd.pass1Start = currentLocationS
                            currentRoute = routeAdd
                            ref.child(currentRouteId!!).setValue(routeAdd)

                            routeInfo.visibility = View.VISIBLE
                            ifIsInRoute = true
                            whichPass = "pass1"
                        }
                        else if (currentRoute.pass2 == "") {
                            val routeAdd = Route(currentRoute)
                            routeAdd.pass2 = username
                            routeAdd.pass2Start = currentLocationS
                            currentRoute = routeAdd
                            ref.child(currentRouteId!!).setValue(routeAdd)

                            routeInfo.visibility = View.VISIBLE
                            ifIsInRoute = true
                            whichPass = "pass2"
                        }
                        else if (currentRoute.pass3 == "") {
                            val routeAdd = Route(currentRoute)
                            routeAdd.pass3 = username
                            routeAdd.pass3Start = currentLocationS
                            currentRoute = routeAdd
                            ref.child(currentRouteId!!).setValue(routeAdd)

                            routeInfo.visibility = View.VISIBLE
                            ifIsInRoute = true
                            whichPass = "pass3"
                        }
                        else if (currentRoute.pass4 == "") {
                            val routeAdd = Route(currentRoute)
                            routeAdd.pass4 = username
                            routeAdd.pass4Start = currentLocationS
                            currentRoute = routeAdd
                            ref.child(currentRouteId!!).setValue(routeAdd)

                            routeInfo.visibility = View.VISIBLE
                            ifIsInRoute = true
                            whichPass = "pass4"
                        }
                        else {
                            Toast.makeText(this, "Nie ma miejsca!", Toast.LENGTH_LONG).show()
                        }


                        true
                    }
                    else -> false
                }
            }
            popup.inflate(R.menu.popup_route)
            popup.show()
        }

    }

    private fun buildLocationCallback() {
        locationCallback = object: LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                var location = p0!!.locations.get(p0!!.locations.size-1)
                currentLocation = LatLng(location!!.latitude, location!!.longitude)
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

}

private class ListViewRouteAdapter(context: Context, routesList: ArrayList<Route>): BaseAdapter() {

    private val mContext: Context
    private val mRoutesList: ArrayList<Route>


    init {
        mContext = context
        mRoutesList = routesList
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(mContext)
        val rowMain = layoutInflater.inflate(R.layout.listview_routes_row, parent, false)
        val startTextView = rowMain.findViewById<TextView>(R.id.start_textView)
        startTextView.text = mRoutesList[position].start_from
        val destinationTextView = rowMain.findViewById<TextView>(R.id.destination_textView)
        destinationTextView.text = mRoutesList[position].destination
        return rowMain
    }

    override fun getItem(position: Int): Any {
        return "TEST STRING"
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mRoutesList.size
    }

}
