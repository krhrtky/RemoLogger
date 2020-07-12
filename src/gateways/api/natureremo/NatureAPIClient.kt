package com.remoLogger.gateways.api.natureremo

import com.remoLogger.entities.sensorRecord.*
import com.remoLogger.gateways.api.natureremo.model.Device
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.util.error
import org.slf4j.LoggerFactory

open class NatureAPIClient(
    private val endpoint: String,
    private val apiAuthKey: String,
    private val client: HttpClient = HttpClient(Apache) {
        install(JsonFeature)
    }
): INatureAPIClient {

    val logger = LoggerFactory.getLogger(this.javaClass)
    /**
     * Fetch the list of Remo devices the user has access to.
     *
     * @return SensorRecords
     */
    override suspend fun getRecords() = runCatching {
        client.get<List<Device>>("$endpoint/1/devices") {
            header("Authorization", "Bearer $apiAuthKey")
        }
    }.fold(
        onSuccess = {
            logger.info(it.toString())
            SensorRecords(it.map {
                SensorRecord.new(
                    Temperature(it.newest_events.te.`val`),
                    Humidity(it.newest_events.hu.`val`),
                    Illuminace(it.newest_events.il.`val`)
                )
            })
        },
        onFailure = {
            logger.error(it)
            SensorRecords(emptyList())
        }
    )
}
