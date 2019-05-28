package com.example.fairride

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_driver_summary.*
import kotlin.math.roundToInt

class DriverSummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_summary)
        val bundle = intent.extras
        lateinit var cost1: String
        lateinit var cost2: String
        lateinit var cost3: String
        lateinit var cost4: String
        lateinit var user1: String
        lateinit var user2: String
        lateinit var user3: String
        lateinit var user4: String

        if( bundle.getString("cost1") != "") {
            val tmp = bundle.getString("cost1").toDouble()
            val tailoredCost = ((tmp * 100).roundToInt().toDouble() / 100).toString()
            cost1 = tailoredCost + "zł"
            cost1text.text =  cost1
        } else {
            cost1text.text = ""
        }
        if( bundle.getString("cost2") != "") {
            val tmp = bundle.getString("cost2").toDouble()
            val tailoredCost = ((tmp * 100).roundToInt().toDouble() / 100).toString()
            cost2 = tailoredCost + "zł"
            cost2text.text =  cost2
        } else {
            cost2text.text = ""
        }
        if( bundle.getString("cost3") != "") {
            val tmp = bundle.getString("cost3").toDouble()
            val tailoredCost = ((tmp * 100).roundToInt().toDouble() / 100).toString()
            cost3 = tailoredCost + "zł"
            cost3text.text =  cost3
        } else {
            cost3text.text = ""
        }
        if( bundle.getString("cost4") != "") {
            val tmp = bundle.getString("cost4").toDouble()
            val tailoredCost = ((tmp * 100).roundToInt().toDouble() / 100).toString()
            cost4 = tailoredCost + "zł"
            cost4text.text =  cost4
        } else {
            cost4text.text = ""
        }
        if( bundle.getString("user1") != "") {
            user1 = bundle.getString("user1")
            pass1text.text =  user1
        } else {
            pass1text.text = ""
        }
        if( bundle.getString("user2") != "") {
            user2 = bundle.getString("user2")
            pass2text.text =  user2
        } else {
            pass2text.text = ""
        }
        if( bundle.getString("user3") != "") {
            user3 = bundle.getString("user3")
            pass3text.text =  user3
        } else {
            pass3text.text = ""
        }
        if( bundle.getString("user4") != "") {
            user4 = bundle.getString("user4")
            pass4text.text =  user4
        } else {
            pass4text.text = ""
        }

    }
}
