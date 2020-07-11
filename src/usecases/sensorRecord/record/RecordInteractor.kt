package com.remoLogger.usecases.sensorRecord.record

import com.remoLogger.entities.sensorRecord.ISensorRecordRepository
import com.remoLogger.entities.sensorRecord.INatureAPIClient
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
            response.forEach { repository.create(it) }
        }
    }
}
