package com.remoLogger.entities.sensorRecord

interface INatureAPIClient {
    suspend fun getRecords(): SensorRecords
}
