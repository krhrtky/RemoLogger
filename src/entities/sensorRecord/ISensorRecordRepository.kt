package com.remoLogger.entities.sensorRecord

import org.joda.time.DateTime

interface ISensorRecordRepository {
    fun create(sensorRecord: SensorRecord)
    fun find(from: DateTime = DateTime.now().minusDays(1), to: DateTime = DateTime.now(), limit: Int = 1000): SensorRecords
}
