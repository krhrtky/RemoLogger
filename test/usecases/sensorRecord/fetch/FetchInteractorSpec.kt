package com.remoLogger.usecases.sensorRecord.fetch

import com.remoLogger.entities.sensorRecord.CreatedAt
import com.remoLogger.entities.sensorRecord.Humidity
import com.remoLogger.entities.sensorRecord.ISensorRecordRepository
import com.remoLogger.entities.sensorRecord.Id
import com.remoLogger.entities.sensorRecord.Illuminace
import com.remoLogger.entities.sensorRecord.SensorRecord
import com.remoLogger.entities.sensorRecord.SensorRecords
import com.remoLogger.entities.sensorRecord.Temperature
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.joda.time.DateTime
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object FetchInteractorSpec: Spek({
    describe(".handle") {
        val sensorRecordRepository = mockk<ISensorRecordRepository>()
        val fetchInteractor = FetchInteractor(sensorRecordRepository)

        context("repository return empty list.") {
            beforeEachTest {
                every { sensorRecordRepository.find(any(), any(), 1000) } returns SensorRecords(emptyList())
            }

            it("should return empty list.") {
                val result = fetchInteractor.handle()
                assertThat(result).isEmpty()
            }
        }

        context("repository return list has 2 items.") {
            beforeEachTest {
                val sensorRecords = (1..2).map {
                    SensorRecord(
                        Id(it.toString()),
                        Temperature(it.toFloat()),
                        Humidity(it.toFloat()),
                        Illuminace(it.toFloat()),
                        CreatedAt(DateTime.now())
                    )
                }
                every { sensorRecordRepository.find(any(), any(), 1000) } returns SensorRecords(sensorRecords)
            }

            it("should return list has 2 items.") {
                val result = fetchInteractor.handle()
                assertThat(result).hasSize(2)
            }
        }
    }
})
