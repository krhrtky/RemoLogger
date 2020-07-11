package com.remoLogger.gateways.api.natureremo

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
     * @return List of Remo devices
     */
    override suspend fun getDevices() = runCatching {
        client.get<List<Device>>("$endpoint/1/devices") {
            header("Authorization", "Bearer $apiAuthKey")
        }
    }.fold(
        onSuccess = {
            logger.info(it.toString())
            it
        },
        onFailure = {
            logger.error(it)
            emptyList()
        }
    )
}
