package com.example.fairride

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_passenger_summary.*
import kotlin.math.roundToInt
import android.content.DialogInterface
import android.support.v7.app.AlertDialog


class PassengerSummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger_summary)

        val bundle = intent.extras
        val cost = bundle.getString("cost").toDouble()
        println(cost)
        val tailoredCost = ((cost * 100).roundToInt().toDouble() / 100).toString()
        println(tailoredCost)
        val displayedCost = tailoredCost + "zł"
        println(displayedCost)

        passengerCost.text = displayedCost

        button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}


