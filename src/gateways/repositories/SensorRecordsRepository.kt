package com.remoLogger.gateways.repositories

import com.remoLogger.entities.sensorRecord.CreatedAt
import com.remoLogger.entities.sensorRecord.Humidity
import com.remoLogger.entities.sensorRecord.ISensorRecordRepository
import com.remoLogger.entities.sensorRecord.Id
import com.remoLogger.entities.sensorRecord.Illuminace
import com.remoLogger.entities.sensorRecord.SensorRecord
import com.remoLogger.entities.sensorRecord.Temperature
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class SensorRecordsRepository: ISensorRecordRepository {

    override fun findAll() = transaction {
        com.remoLogger.entities.sensorRecord.SensorRecords(SensorRecords.selectAll().map { convertToSensorRecord(it) })
    }

    private fun convertToSensorRecord(resultRow: ResultRow) = SensorRecord(
            Id(resultRow[SensorRecords.id]),
            Temperature(resultRow[SensorRecords.temperature]),
            Humidity(resultRow[SensorRecords.humidity]),
            Illuminace(resultRow[SensorRecords.illuminance]),
            CreatedAt(resultRow[SensorRecords.createdAt])
    )
}
