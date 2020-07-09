package com.remoLogger

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.remoLogger.controllers.sensorRecords
import com.remoLogger.externals.di.DIContainer
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.request.path
import io.ktor.routing.routing
import com.remoLogger.gateways.repositories.DBFactory
import com.typesafe.config.ConfigFactory
import io.ktor.application.call
import io.ktor.config.HoconApplicationConfig
import io.ktor.response.respond
import io.ktor.routing.get
import org.koin.ktor.ext.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.event.Level
import org.koin.core.logger.Level.INFO


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val config = HoconApplicationConfig(ConfigFactory.load())

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(Koin) {
        slf4jLogger(INFO)
        modules(DIContainer.sensorRecords)
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
        sensorRecords()
    }
}
