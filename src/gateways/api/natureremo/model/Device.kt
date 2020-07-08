package com.remoLogger.gateways.api.natureremo.model

import java.util.Date

data class Device(
    val id: String,
    val name: String,
    val temperature_offset: Double,
    val humidity_offset: Double,
    val created_at: Date,
    val updated_at: Date,
    val firmware_version: String,
    val mac_address: String,
    val serial_number: String,
    val newest_events: DeviceInfo,
    val users: List<User>
)
