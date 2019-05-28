package com.example.fairride

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_passenger_summary.*
import kotlin.math.roundToInt

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
    }
}
