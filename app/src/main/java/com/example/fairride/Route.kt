package com.example.fairride

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Route(
    var consumption: String? = "",
    var driver: String? = "",
    var start_from: String? = "",
    var destination: String? = "",
    var pass1Start: String? = "",
    var pass1End: String? = "",
    var pass2Start: String? = "",
    var pass2End: String? = "",
    var pass3Start: String? = "",
    var pass3End: String? = "",
    var pass4Start: String? = "",
    var pass4End: String? = "",
    var pass1: String? = "",
    var pass2: String? = "",
    var pass3: String? = "",
    var pass4: String? = ""


    ) {
    constructor(consumption: String, driver: String, start_from: String?, destination: String?) : this() {
        this.consumption = consumption
        this.driver = driver
        this.start_from = start_from
        this.destination = destination

    }

}
