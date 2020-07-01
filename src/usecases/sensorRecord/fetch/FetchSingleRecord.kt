package com.remoLogger.usecases.sensorRecord.fetch

import org.joda.time.DateTime

data class FetchSingleRecord(
        val id: String,
        val temperature: Float,
        val humidity: Float,
        val illuminance: Float,
        val createdAt: DateTime
)
