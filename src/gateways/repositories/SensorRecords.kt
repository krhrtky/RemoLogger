package com.remoLogger.gateways.repositories

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object SensorRecords: Table("sensor_records") {
    val id: Column<String> = varchar("id", 50).uniqueIndex()
    val temperature: Column<Float> = float("temperature")
    val humidity: Column<Float> = float("humidity")
    val illuminance: Column<Float> = float("illuminance")
    val createdAt: Column<DateTime> = datetime("created_at")
}
