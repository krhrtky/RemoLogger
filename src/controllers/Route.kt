package com.remoLogger.controllers

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

fun Route.sensorRecords(controller: SensorRecordsController) {

    get("/sensorRecords") {
        call.respond(controller.fetchAll())
    }
}
