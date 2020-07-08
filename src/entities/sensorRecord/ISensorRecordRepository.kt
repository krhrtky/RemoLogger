package com.remoLogger.entities.sensorRecord

interface ISensorRecordRepository {
    fun create(sensorRecord: SensorRecord): Unit
    fun findAll(): SensorRecords
}
