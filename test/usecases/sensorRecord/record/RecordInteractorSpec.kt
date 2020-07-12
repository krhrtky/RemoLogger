package usecases.sensorRecord.record

import com.remoLogger.entities.sensorRecord.Humidity
import com.remoLogger.entities.sensorRecord.INatureAPIClient
import com.remoLogger.entities.sensorRecord.ISensorRecordRepository
import com.remoLogger.entities.sensorRecord.Illuminace
import com.remoLogger.entities.sensorRecord.SensorRecord
import com.remoLogger.entities.sensorRecord.SensorRecords
import com.remoLogger.entities.sensorRecord.Temperature
import com.remoLogger.usecases.sensorRecord.record.RecordInteractor
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.jetbrains.exposed.sql.Database
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object RecordInteractorSpec: Spek({
    describe(".handle") {
        val repository = mockk<ISensorRecordRepository>()
        val client = mockk<INatureAPIClient>()
        val interactor = RecordInteractor(client, repository)

        beforeEachGroup {
            Database.connect("jdbc:h2:mem:test;MODE=MySQL", driver = "org.h2.Driver")
        }

        context("repository return value has item.") {

            val sensorRecord = SensorRecord.new(
                Temperature(25.0.toFloat()),
                Humidity(70.0.toFloat()),
                Illuminace(130.toFloat())
            )
            val sensorRecords = SensorRecords(listOf(sensorRecord))

            beforeGroup {
                coEvery { client.getRecords() } returns sensorRecords
                every { repository.create(sensorRecord) } just Runs
            }

            it("should not occur error.") {
                interactor.handle()
                coVerify(exactly = 1) { client.getRecords() }
                verify(exactly = 1) { repository.create(sensorRecord) }
                confirmVerified(client, repository)

            }

        }
    }
})
