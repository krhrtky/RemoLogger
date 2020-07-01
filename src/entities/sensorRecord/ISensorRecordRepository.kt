package com.remoLogger.entities.sensorRecord

interface ISensorRecordRepository {
    fun findAll(): SensorRecords
}
