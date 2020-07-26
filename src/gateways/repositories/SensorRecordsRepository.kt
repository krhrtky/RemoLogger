package com.remoLogger.gateways.repositories

import com.remoLogger.entities.sensorRecord.CreatedAt
import com.remoLogger.entities.sensorRecord.Humidity
import com.remoLogger.entities.sensorRecord.ISensorRecordRepository
import com.remoLogger.entities.sensorRecord.Id
import com.remoLogger.entities.sensorRecord.Illuminace
import com.remoLogger.entities.sensorRecord.SensorRecord
import com.remoLogger.entities.sensorRecord.SensorRecords as Domains
import com.remoLogger.entities.sensorRecord.Temperature
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class SensorRecordsRepository: ISensorRecordRepository {

    override fun create(sensorRecord: SensorRecord) {
        SensorRecords.insert {
            it[id] = sensorRecord.id.value
            it[temperature] = sensorRecord.temperature.value
            it[humidity] = sensorRecord.humidity.value
            it[illuminance] = sensorRecord.illuminance.value
            it[createdAt] = sensorRecord.createdAt.value
        }
    }

    override fun find(from: DateTime, to: DateTime, limit: Int) = transaction {
        Domains(
            SensorRecords.select { SensorRecords.createdAt.between(from, to) }
            .limit(limit)
            .map { convertToSensorRecord(it) }
        )
    }

    private fun convertToSensorRecord(resultRow: ResultRow) = SensorRecord(
            Id(resultRow[SensorRecords.id]),
            Temperature(resultRow[SensorRecords.temperature]),
            Humidity(resultRow[SensorRecords.humidity]),
            Illuminace(resultRow[SensorRecords.illuminance]),
            CreatedAt(resultRow[SensorRecords.createdAt])
    )
}
