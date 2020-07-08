package com.remoLogger.gateways.api.natureremo

import com.remoLogger.gateways.api.natureremo.model.Device

interface INatureAPIClient {
    suspend fun getDevices(): List<Device>
}
