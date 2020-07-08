package com.remoLogger

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.remoLogger.controllers.SensorRecordsController
import com.remoLogger.controllers.sensorRecords
import com.remoLogger.gateways.api.natureremo.NatureAPIClient
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.request.path
import io.ktor.routing.routing
import com.remoLogger.gateways.repositories.DBFactory
import com.remoLogger.gateways.repositories.SensorRecordsRepository
import com.remoLogger.usecases.sensorRecord.fetch.FetchInteractor
import com.remoLogger.usecases.sensorRecord.record.RecordInteractor
import com.typesafe.config.ConfigFactory
import io.ktor.application.call
import io.ktor.config.HoconApplicationConfig
import io.ktor.response.respond
import io.ktor.routing.get
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val config = HoconApplicationConfig(ConfigFactory.load())

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            registerModule(JodaModule())
        }
    }

    DBFactory.init()

    routing {
        get("/") {
            call.respond("")
        }
        sensorRecords(
            SensorRecordsController(
                FetchInteractor(
                    SensorRecordsRepository()
                ),
                RecordInteractor(
                    NatureAPIClient(
                        config.propertyOrNull("ktor.api.remo.endpoint")?.getString() ?: "",
                        config.propertyOrNull("ktor.api.remo.authKey")?.getString() ?: ""
                    ),
                    SensorRecordsRepository()
                )
            )
        )
    }
}
