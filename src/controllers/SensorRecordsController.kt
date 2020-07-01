package com.remoLogger.controllers

import com.remoLogger.usecases.sensorRecord.fetch.IFetchUseCase

class SensorRecordsController(private val fetchUseCase: IFetchUseCase) {

    fun fetchAll() = fetchUseCase.handle()
}

