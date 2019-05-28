package com.example.fairride

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.firebase.client.DataSnapshot
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class DriverSettingsActivity : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var keyList: ArrayList<String>
    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_settings)

        val bundle = intent.extras
        val username = bundle.getString("username")
        listView = findViewById(R.id.listView)
        keyList = arrayListOf()
        ref = FirebaseDatabase.getInstance().getReference("cars")



        ref.addValueEventListener(object : ValueEventListener, com.google.firebase.database.ValueEventListener {
            override fun onCancelled(p0: FirebaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {

            }

            override fun onDataChange(p0: com.google.firebase.database.DataSnapshot) {
                if (p0!!.exists()) {
                    keyList.clear()
                    for (e in p0.children) {
                        keyList.add(e.key!!)
                        println(keyList)
                    }

                    listView.adapter = CarsAdapter(applicationContext, keyList)
                }

            }

            override fun onCancelled(p0: DatabaseError) {
            }

        })

        var query = ref
        listView.setOnItemClickListener { adapterView, view, i, l ->
            //Toast.makeText(applicationContext, "You chose: " + keyList[i], Toast.LENGTH_SHORT).show()

            query = query.child(keyList[i])
            query.addValueEventListener(object : ValueEventListener, com.google.firebase.database.ValueEventListener {
                override fun onCancelled(p0: FirebaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: com.google.firebase.database.DataSnapshot) {
                    if (p0!!.exists()) {
                        keyList.clear()
                        for (e in p0.children) {
                            keyList.add(e.key!!)
                            try {
                                if (keyList[i] == "consumption") {
                                    Toast.makeText(applicationContext, "Consumption: " + e.value, Toast.LENGTH_SHORT)
                                        .show()
                                    val intent = Intent(applicationContext, MapsActivityDriver::class.java)
                                    intent.putExtra("username", username)
                                    intent.putExtra("consumption", e.value.toString().toDouble())
                                    startActivity(intent)
                                }
                            } catch (t:Throwable) {

                            }
                        }

                        listView.adapter = CarsAdapter(applicationContext, keyList)

                    }
                }

            })
        }
    }
}

private class CarsAdapter(context: Context, keyList: ArrayList<String>): BaseAdapter() {

    private val mContext: Context
    private val mKeyList: ArrayList<String>


    init {
        mContext = context
        mKeyList = keyList
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(mContext)
        val rowMain = layoutInflater.inflate(R.layout.cars, parent, false)
        val startTextView = rowMain.findViewById<TextView>(R.id.carText)
        startTextView.text = mKeyList[position]
        return rowMain
    }

    override fun getItem(position: Int): Any {
        return "TEST STRING"
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mKeyList.size
    }


}
