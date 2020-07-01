package com.remoLogger.usecases.sensorRecord.fetch

import com.remoLogger.entities.sensorRecord.ISensorRecordRepository

class FetchInteractor(private val repository: ISensorRecordRepository): IFetchUseCase {

    override fun handle(): List<FetchSingleRecord> = repository
            .findAll()
            .value
            .map {
                FetchSingleRecord(
                        it.id.value,
                        it.temperature.value,
                        it.humidity.value,
                        it.illuminance.value,
                        it.createdAt.value
                )
            }
}
