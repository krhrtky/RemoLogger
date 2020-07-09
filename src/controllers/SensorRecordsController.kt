package com.remoLogger.controllers

import com.remoLogger.usecases.sensorRecord.fetch.IFetchUseCase
import com.remoLogger.usecases.sensorRecord.record.IRecordUseCase
import org.koin.core.KoinComponent
import org.koin.core.get

class SensorRecordsController: KoinComponent {
    private val fetchUseCase: IFetchUseCase = get()
    private val recordUseCase: IRecordUseCase = get()

    fun record() = recordUseCase.handle()

    fun fetchAll() = fetchUseCase.handle()
}

