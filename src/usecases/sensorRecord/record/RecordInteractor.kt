package com.remoLogger.usecases.sensorRecord.record

import com.remoLogger.entities.sensorRecord.*
import com.remoLogger.gateways.api.natureremo.INatureAPIClient
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

class RecordInteractor(
    private val client: INatureAPIClient,
    private val repository: ISensorRecordRepository
): IRecordUseCase {

    override fun handle() {
        val response = runBlocking {
            client.getDevices()
        }
        transaction {
            addLogger(StdOutSqlLogger)
            response
                .map {
                    SensorRecord.new(
                        Temperature(it.newest_events.te.`val`),
                        Humidity(it.newest_events.hu.`val`),
                        Illuminace(it.newest_events.il.`val`)
                    )
                }
                .forEach {
                    repository.create(it)
                }
        }
    }
}
