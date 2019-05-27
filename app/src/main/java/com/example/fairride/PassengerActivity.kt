package com.example.fairride

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.SupportActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.firebase.client.DataSnapshot
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.json.JSONObject

class PassengerActivity : AppCompatActivity() {


    lateinit var ref : DatabaseReference
    lateinit var keyList: ArrayList<String>
    lateinit var routesList: ArrayList<Route>
    lateinit var jsonResponse: JSONObject




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger)
        ref = FirebaseDatabase.getInstance().getReference("routes")
        var username = FirebaseAuth.getInstance().currentUser!!.displayName
        keyList = arrayListOf()
        routesList = arrayListOf()
        val listView = findViewById<ListView>(R.id.listViewRoutes)

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

        listView.setOnItemClickListener { parent, view, position, id ->
            val routeId = keyList[position]
            val route = routesList[position]
            val routeAdd = Route(route.consumption, route.driver, route.start_from, route.destination)
            routeAdd.pass1 = username
            ref.child(routeId!!).setValue(routeAdd)
        }


/*        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
        })*/
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
