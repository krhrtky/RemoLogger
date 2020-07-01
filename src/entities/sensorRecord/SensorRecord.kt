package com.remoLogger.entities.sensorRecord

class SensorRecord(
    val id: Id,
    val temperature: Temperature,
    val humidity: Humidity,
    val illuminance: Illuminace,
    val createdAt: CreatedAt
)
