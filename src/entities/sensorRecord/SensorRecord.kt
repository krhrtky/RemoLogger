package com.remoLogger.entities.sensorRecord

import org.joda.time.DateTime
import java.util.UUID

class SensorRecord(
    val id: Id,
    val temperature: Temperature,
    val humidity: Humidity,
    val illuminance: Illuminace,
    val createdAt: CreatedAt
) {
    companion object {
        fun new(temperature: Temperature, humidity: Humidity, illuminance: Illuminace) = SensorRecord(
            Id(UUID.randomUUID().toString()),
            temperature,
            humidity,
            illuminance,
            CreatedAt(DateTime.now())
        )
    }
}
