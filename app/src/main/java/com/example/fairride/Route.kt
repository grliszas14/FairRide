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
    var pass4: String? = "",
    var pass1cost: String? = "",
    var pass2cost: String? = "",
    var pass3cost: String? = "",
    var pass4cost: String? = "",
    var pass1inout: String? = "",
    var pass2inout: String? = "",
    var pass3inout: String? = "",
    var pass4inout: String? = "",
    var lastCheckpoint: String? = "",
    var routeDone: String? = ""

    ) {
    constructor(consumption: String, driver: String, start_from: String, destination: String, lastCheckpoint: String) : this() {
        this.consumption = consumption
        this.driver = driver
        this.start_from = start_from
        this.destination = destination
        this.lastCheckpoint = lastCheckpoint

    }
    constructor(route: Route) : this() {
        this.consumption = route.consumption
        this.driver = route.driver
        this.destination = route.destination
        this.start_from = route.start_from
        this.pass1 = route.pass1
        this.pass2 = route.pass2
        this.pass3 = route.pass3
        this.pass4 = route.pass4
        this.pass1cost = route.pass1cost
        this.pass2cost = route.pass2cost
        this.pass3cost = route.pass3cost
        this.pass4cost = route.pass4cost
        this.pass1inout = route.pass1inout
        this.pass2inout = route.pass2inout
        this.pass3inout = route.pass3inout
        this.pass4inout = route.pass4inout
        this.pass1Start = route.pass1Start
        this.pass1End = route.pass1End
        this.pass2Start = route.pass2Start
        this.pass2End = route.pass2End
        this.pass3Start = route.pass3Start
        this.pass3End = route.pass3End
        this.pass4Start= route.pass4Start
        this.pass4End = route.pass4End
        this.lastCheckpoint = route.lastCheckpoint
        this.routeDone = route.routeDone
    }

}
