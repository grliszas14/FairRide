package com.example.fairride

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.firebase.client.DataSnapshot
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.util.Log
import android.widget.ListAdapter

class DriverSettingsActivity : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var keyList: ArrayList<String>
    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_settings)

        listView = findViewById(R.id.listView)
        keyList = arrayListOf()
        ref = FirebaseDatabase.getInstance().getReference("cars")

        listView.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(applicationContext, "You chose: " + keyList[i], Toast.LENGTH_SHORT).show()
        }

        ref.addValueEventListener(object : ValueEventListener, com.google.firebase.database.ValueEventListener {
            override fun onCancelled(p0: FirebaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {

            }

            override fun onDataChange(p0: com.google.firebase.database.DataSnapshot) {
                Log.d("TAG", "message")

                if (p0!!.exists()) {
                    keyList.clear()
                    for (e in p0.children) {
                        keyList.add(e.key!!)
                        println(keyList)
                    }

                    listView.adapter =  ArrayAdapter<String>(applicationContext, android.R.layout.simple_list_item_1, keyList)
                }

            }

            override fun onCancelled(p0: DatabaseError) {
            }

        })
    }
}
