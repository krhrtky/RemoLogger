package com.remoLogger.gateways.api.natureremo.model

import java.util.Date

data class SensorValue(
    val `val`: Float,
    val created_at: Date
)
