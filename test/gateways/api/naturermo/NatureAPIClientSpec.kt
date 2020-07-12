package com.remoLogger.gateways.api.naturermo

import com.remoLogger.gateways.api.natureremo.NatureAPIClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.http.hostWithPort
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe



object NatureAPIClientSpec: Spek({

    describe(".getRecords") {
        context("has valid response") {
            val httpClient = HttpClient(MockEngine) {
                install(JsonFeature) {
                    serializer = JacksonSerializer()
                }
                engine {
                    addHandler { request ->
                        when (request.url.fullUrl) {
                            "https://api.nature.global/1/devices" -> {
                                val responseHeaders = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                                respond("""
                                [
                                  {
                                    "name": "test-name",
                                    "id": "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz",
                                    "created_at": "2020-01-04T05:33:30Z",
                                    "updated_at": "2020-07-10T15:01:01Z",
                                    "mac_address": "3d:g1:42:7k:55:32",
                                    "serial_number": "xxxxxxxxxxxxxx",
                                    "firmware_version": "Remo/1.0.77-g808448c",
                                    "temperature_offset": 0,
                                    "humidity_offset": 0,
                                    "users": [
                                      {
                                        "id": "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy",
                                        "nickname": "test-user",
                                        "superuser": true
                                      }
                                    ],
                                    "newest_events": {
                                      "hu": {
                                        "val": 76,
                                        "created_at": "2020-07-11T15:09:42Z"
                                      },
                                      "il": {
                                        "val": 135,
                                        "created_at": "2020-07-11T15:24:43Z"
                                      },
                                      "mo": {
                                        "val": 1,
                                        "created_at": "2020-07-11T04:07:13Z"
                                      },
                                      "te": {
                                        "val": 27.2,
                                        "created_at": "2020-07-11T06:43:41Z"
                                      }
                                    }
                                  }
                                ]

                            """.trimIndent(), headers = responseHeaders)
                            }
                            else -> error("Unhandled ${request.url.fullUrl}")
                        }
                    }
                }
            }
            it("should return SensorRecords has 1 item.") {

                val client = NatureAPIClient(
                    "https://api.nature.global",
                    "xxxx",
                    httpClient
                )

                val result = runBlocking { client.getRecords() }
                Assertions.assertThat(result).hasSize(1)
            }
        }

        context("has error response") {
            val httpClient = HttpClient(MockEngine) {
                install(JsonFeature) {
                    serializer = JacksonSerializer()
                }
                engine {
                    addHandler { request ->
                        when (request.url.fullUrl) {
                            "https://api.nature.global/1/devices" -> {
                                respondError(HttpStatusCode.Unauthorized)
                            }
                            else -> error("Unhandled ${request.url.fullUrl}")
                        }
                    }
                }
            }
            it("should return SensorRecords has 1 item.") {

                val client = NatureAPIClient(
                    "https://api.nature.global",
                    "xxxx",
                    httpClient
                )

                val result = runBlocking { client.getRecords() }
                Assertions.assertThat(result).isEmpty()
            }
        }

    }
})

val Url.hostWithPortIfRequired: String get() = if (port == protocol.defaultPort) host else hostWithPort
val Url.fullUrl: String get() = "${protocol.name}://$hostWithPortIfRequired$fullPath"
