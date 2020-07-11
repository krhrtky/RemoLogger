package com.remoLogger.externals.di

import com.remoLogger.entities.sensorRecord.ISensorRecordRepository
import com.remoLogger.entities.sensorRecord.INatureAPIClient
import com.remoLogger.gateways.api.natureremo.NatureAPIClient
import com.remoLogger.gateways.repositories.SensorRecordsRepository
import com.remoLogger.usecases.sensorRecord.fetch.FetchInteractor
import com.remoLogger.usecases.sensorRecord.fetch.IFetchUseCase
import com.remoLogger.usecases.sensorRecord.record.IRecordUseCase
import com.remoLogger.usecases.sensorRecord.record.RecordInteractor
import com.typesafe.config.ConfigFactory
import io.ktor.config.HoconApplicationConfig
import org.koin.dsl.module

class DIContainer {
    companion object {
        val sensorRecords = module {
            val config = HoconApplicationConfig(ConfigFactory.load())

            single<ISensorRecordRepository> { SensorRecordsRepository() }
            single<INatureAPIClient> {
                NatureAPIClient(
                    config.propertyOrNull("ktor.api.remo.endpoint")?.getString() ?: "",
                    config.propertyOrNull("ktor.api.remo.authKey")?.getString() ?: ""
                )
            }
            single<IFetchUseCase> { FetchInteractor(get()) }
            single<IRecordUseCase> { RecordInteractor(get(), get()) }
        }
    }
}
