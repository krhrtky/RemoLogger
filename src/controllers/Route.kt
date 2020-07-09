package com.remoLogger.controllers

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.header
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

fun Route.sensorRecords(controller: SensorRecordsController = SensorRecordsController()) {

    get("/record") {
        val appengineCronHeader = call.request.header("X-Appengine-Cron")?.toBoolean()

        if (appengineCronHeader != null && appengineCronHeader) {
            controller.record()
            return@get call.respond(HttpStatusCode.OK)
        } else {
            return@get call.respond(HttpStatusCode.NotFound)
        }
    }

    get("/sensorRecords") {
        call.respond(controller.fetchAll())
    }
}
