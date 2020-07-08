package com.remoLogger.controllers

import com.remoLogger.usecases.sensorRecord.fetch.IFetchUseCase
import com.remoLogger.usecases.sensorRecord.record.IRecordUseCase

class SensorRecordsController(
    private val fetchUseCase: IFetchUseCase,
    private val recordUseCase: IRecordUseCase
) {

    fun record() = recordUseCase.handle()

    fun fetchAll() = fetchUseCase.handle()
}

