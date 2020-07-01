package com.remoLogger.usecases.sensorRecord.fetch

import com.remoLogger.entities.sensorRecord.ISensorRecordRepository

interface IFetchUseCase {
    fun handle(): List<FetchSingleRecord>
}
