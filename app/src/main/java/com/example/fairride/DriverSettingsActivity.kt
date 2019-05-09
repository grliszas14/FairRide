package com.example.fairride

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class DriverSettingsActivity : AppCompatActivity() {

    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_settings)

        listView = findViewById(R.id.listView)

        val cars = arrayOf("Audi", "BMW", "Opel", "Seat")

        listView.adapter =  ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cars)

        listView.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(applicationContext, "You chose: " + cars[i], Toast.LENGTH_SHORT).show()
        }
    }
}
